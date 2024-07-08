package com.example.data.remote

import com.example.data.remote.model.RepoResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val IN_QUALIFIER = "name,description"

interface GithubApi {
    @GET("search/repositories")
    suspend fun searchRepo(
        @Query("q") query: String,
        @Query("in") inQualifier: String = IN_QUALIFIER,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
    ): RepoResponse
}
