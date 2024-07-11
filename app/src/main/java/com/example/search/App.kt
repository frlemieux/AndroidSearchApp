package com.example.search

import android.app.Application
import com.example.data.di.dataModule
import com.example.search.di.featureSearchModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        initKoin()
        super.onCreate()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            androidFileProperties()
            modules(
                listOf(
                    dataModule,
                    featureSearchModule,
                ),
            )
        }
    }
}
