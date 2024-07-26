package com.example.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.data.local.RepoDatabase
import com.example.data.local.entity.RepoEntity
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 0

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val query: String,
    private val githubApi: GithubApi,
    private val repoDatabase: RepoDatabase,
) : RemoteMediator<Int, RepoEntity>() {
    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoEntity>,
    ): MediatorResult {
        val page =
            when (loadType) {
                LoadType.REFRESH -> GITHUB_STARTING_PAGE_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true,
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        GITHUB_STARTING_PAGE_INDEX
                    } else {
                        (lastItem.index / state.config.pageSize)
                    }
                }
            }
        try {
            val apiResponse =
                githubApi.searchRepo(
                    query = query,
                    page = page,
                    itemsPerPage = state.config.pageSize,
                )
            val repos = apiResponse.items
            val endOfPaginationReached = repos.isEmpty()
            repoDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    resetDb()
                }
                repoDatabase.reposDao.insertAll(repos.map { it.toEntity() })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            resetDb()
            val errorBody = exception.response()?.errorBody()?.string()
            val gson = Gson()
            val errorResponse = gson.fromJson(errorBody, GitHubErrorResponse::class.java)
            return MediatorResult.Error(Throwable(errorResponse.errorMessage()))
        }
    }

    private suspend fun resetDb() {
        repoDatabase.reposDao.clearRepos()
        repoDatabase.reposDao.resetPrimaryKey()
    }
}
