package com.example.search.di

import com.example.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureSearchModule =
    module {
        viewModel { SearchViewModel(get(), get()) }
    }
