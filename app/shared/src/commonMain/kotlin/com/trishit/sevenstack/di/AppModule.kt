package com.trishit.sevenstack.di

import com.trishit.sevenstack.database.SevenStackDatabase
import com.trishit.sevenstack.database.getRoomDatabase
import com.trishit.sevenstack.datastore.DataStoreFactory
import com.trishit.sevenstack.repository.AppRepository
import com.trishit.sevenstack.repository.SettingsRepository
import com.trishit.sevenstack.ui.viewmodel.AppViewModel
import com.trishit.sevenstack.ui.viewmodel.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { getRoomDatabase(factory = get()) }
    single { get<SevenStackDatabase>().appDao() }
    single { AppRepository(dao = get()) }
    viewModelOf(
        ::AppViewModel,
    )
    single { get<DataStoreFactory>().createDatastore() }
    single { SettingsRepository(dataStore = get()) }
    viewModelOf(
        ::SettingsViewModel,
    )
}
expect val platformModule: Module