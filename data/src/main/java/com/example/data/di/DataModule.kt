package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.RepoDatabase
import com.example.data.remote.GithubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideGithubRetrofit(): Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder().addInterceptor(logger).build()
        return Retrofit
            .Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideContactApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

    @Provides
    @Singleton
    fun provideContactDatabase(
        @ApplicationContext context: Context,
    ): RepoDatabase =
        Room
            .databaseBuilder(
                context,
                RepoDatabase::class.java,
                "github.db",
            ).build()

    @Provides
    @Singleton
    fun provideDefaultCoroutineScope(): CoroutineContext = Dispatchers.Default
}
