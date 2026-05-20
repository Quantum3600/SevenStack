package com.trishit.sevenstack.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import okio.Path.Companion.toPath

actual class DataStoreFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDatastore(): DataStore<Preferences> {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val path = requireNotNull(documentDirectory?.path) + "/sevenstack.preferences_pb"

        return PreferenceDataStoreFactory.createWithPath(
            produceFile = { path.toPath() }
        )
    }
}