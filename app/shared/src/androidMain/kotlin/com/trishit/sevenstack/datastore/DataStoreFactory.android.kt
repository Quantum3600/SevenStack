package com.trishit.sevenstack.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.FileSystem
import java.io.File

actual class DataStoreFactory(private val ctx: Context) {
    actual fun createDatastore(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                File(ctx.filesDir, "sevenstack.preferences_pb")
            }
        )
    }
}