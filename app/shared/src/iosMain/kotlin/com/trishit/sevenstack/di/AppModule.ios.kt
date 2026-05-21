package com.trishit.sevenstack.di

import com.trishit.sevenstack.database.DatabaseFactory
import com.trishit.sevenstack.datastore.DataStoreFactory
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseFactory() }
    single { DataStoreFactory() }
}

fun initKoinIOS() {
    startKoin {
        modules(appModule, platformModule)
    }
}