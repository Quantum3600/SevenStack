package com.trishit.sevenstack.database

import androidx.room.RoomDatabaseConstructor

expect object SevenStackDatabaseConstructor : RoomDatabaseConstructor<SevenStackDatabase> {
    override fun initialize(): SevenStackDatabase
}