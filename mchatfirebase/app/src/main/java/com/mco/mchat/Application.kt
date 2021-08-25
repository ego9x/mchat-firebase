package com.mco.mchat

import android.app.Application
import com.mco.mchat.di.appModule
import com.mco.mchat.di.repositoryModule
import com.mco.mchat.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            //TODO:
        }

        startKoin {
            androidContext(this@Application)
            modules(arrayListOf(appModule, viewModelModule, repositoryModule))
        }
    }
}