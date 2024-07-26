package com.example.data.di

import com.example.data.repository.RepoRepositoryImpl
import com.example.domain.repository.RepoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewComponent

@Module
@InstallIn(ViewComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepoRepository(repoRepositoryImpl: RepoRepositoryImpl): RepoRepository
}
