package com.trishit.sevenstack.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSGlobalDomain
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createBuilder(): RoomDatabase.Builder<SevenStackDatabase> {
        val documentDirectory: NSURL = NSFileManager.defaultManager.URLForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask,
            null,
            false,
            null
        )!!
        val dbFilePath = documentDirectory.path + "/$DATABASE_NAME"
        return Room.databaseBuilder<SevenStackDatabase>(
            name = dbFilePath,
            factory = { SevenStackDatabaseConstructor.initialize() }
        )
    }
}