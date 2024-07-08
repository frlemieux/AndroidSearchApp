package com.example.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.local.RepoDatabase
import com.example.data.remote.GithubApi
import com.example.data.remote.GithubRemoteMediator
import com.example.domain.model.Repo
import com.example.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepoRepositoryImpl
    @Inject
    constructor(
        private val repoApi: GithubApi,
        private val repoDatabase: RepoDatabase,
    ) : RepoRepository {
        override fun getPagingDataRepo(query: String): Flow<PagingData<Repo>> {
            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator =
                    GithubRemoteMediator(
                        query,
                        repoApi,
                        repoDatabase,
                    ),
                pagingSourceFactory = { repoDatabase.reposDao.repoEntityPagingSource() },
            ).flow.map { pagingData ->
                pagingData.map { it.toDomain() }
            }
        }
    }
