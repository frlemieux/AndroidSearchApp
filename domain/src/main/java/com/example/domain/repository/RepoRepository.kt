package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.Repo
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    fun getPagingDataRepo(query: String): Flow<PagingData<Repo>>
}
