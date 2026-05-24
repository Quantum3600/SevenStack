package com.trishit.sevenstack.di

import com.trishit.sevenstack.database.DatabaseFactory
import com.trishit.sevenstack.datastore.DataStoreFactory
import com.trishit.sevenstack.auth.GoogleAuthEngine
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseFactory(ctx = androidContext()) }
    single { DataStoreFactory(ctx = androidContext()) }
    single { GoogleAuthEngine(context = androidContext()) }
}
