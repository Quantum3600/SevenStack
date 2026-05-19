package com.trishit.sevenstack.di

import com.trishit.sevenstack.database.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseFactory() }
}