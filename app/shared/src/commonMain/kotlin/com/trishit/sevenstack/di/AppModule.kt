package com.trishit.sevenstack.di

import com.trishit.sevenstack.database.SevenStackDatabase
import com.trishit.sevenstack.database.getRoomDatabase
import com.trishit.sevenstack.repository.AppRepository
import com.trishit.sevenstack.ui.viewmodel.AppViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val AppModule = module {
    single { getRoomDatabase(factory = get()) }
    single { get<SevenStackDatabase>().appDao() }
    single { AppRepository(dao = get()) }
    viewModelOf(
        ::AppViewModel,
    )
}
expect val platformModule: Module