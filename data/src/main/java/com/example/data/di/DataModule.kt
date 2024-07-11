package com.example.data.di

import androidx.room.Room
import com.example.data.local.RepoDatabase
import com.example.data.remote.GithubApi
import com.example.data.repository.RepoRepositoryImpl
import com.example.domain.repository.RepoRepository
import io.reactivex.rxjava3.schedulers.Schedulers.single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule =
    module {
        single {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC
            val client = OkHttpClient.Builder().addInterceptor(logger).build()
            Retrofit
                .Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        single {
            val retrofit: Retrofit = get()
            retrofit.create(GithubApi::class.java)
        }

        single {
            Room.databaseBuilder(androidContext(), RepoDatabase::class.java, "github.db").build()
        }

        single {
            val contactApi: GithubApi = get()
            val contactDatabase: RepoDatabase = get()
            RepoRepositoryImpl(contactApi, contactDatabase) as RepoRepository
        }
    }
