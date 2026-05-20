package com.trishit.sevenstack.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DataStoreFactory {
    fun createDatastore(): DataStore<Preferences>
}