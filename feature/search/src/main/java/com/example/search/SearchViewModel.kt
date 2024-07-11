package com.example.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.domain.model.Repo
import com.example.domain.repository.RepoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class SearchViewModel
    constructor(
        private val searchRepository: RepoRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")
        private val search = MutableStateFlow(searchQuery.value)

        fun onSearchQueryChanged(query: String) {
            savedStateHandle[SEARCH_QUERY] = query
        }

        fun onSearchTriggered(query: String) {
            search.update {
                query
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        val pagingData: Flow<PagingData<RepoItem>> =
            search
                .flatMapLatest {
                    searchRepository
                        .getPagingDataRepo(query = it)
                        .cachedIn(viewModelScope)
                        .map {
                            it.map {
                                it.toRepoUiState()
                            }
                        }
                }
    }

private fun Repo.toRepoUiState(): RepoItem =
    RepoItem(
        id = id,
        name = name,
        fullName = fullName,
        description = description,
        url = url,
        stars = stars,
        forks = forks,
        language = language,
    )

private const val SEARCH_QUERY = "searchQuery"
