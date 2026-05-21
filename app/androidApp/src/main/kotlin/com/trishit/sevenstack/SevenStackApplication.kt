package com.trishit.sevenstack

import android.app.Application
import com.trishit.sevenstack.di.appModule
import com.trishit.sevenstack.di.platformModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class SevenStackApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SevenStackApplication)
            modules(appModule, platformModule)
        }
    }
}