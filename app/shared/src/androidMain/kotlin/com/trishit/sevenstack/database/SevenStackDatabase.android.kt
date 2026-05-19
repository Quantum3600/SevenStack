package com.trishit.sevenstack.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(private val ctx: Context) {
    actual fun createBuilder(): RoomDatabase.Builder<SevenStackDatabase> {
        val dbFile = ctx.getDatabasePath(DATABASE_NAME)
        return Room.databaseBuilder<SevenStackDatabase>(
            context = ctx,
            name = dbFile.absolutePath
        )
    }
}