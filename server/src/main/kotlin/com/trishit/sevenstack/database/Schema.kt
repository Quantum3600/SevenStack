package com.trishit.sevenstack.database

import org.jetbrains.exposed.v1.core.Table

object Users: Table("users") {
    val id = varchar("id", 64)
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 128)
    val profilePicUrl = text("profie_pic_url").nullable()
    override val primaryKey = PrimaryKey(id)
}

object Days: Table("days") {
    val id = varchar("id", 64)
    val userId = varchar("user_id", 64).references(Users.id)
    val date = varchar("date", 32)
    override val primaryKey = PrimaryKey(id)
}

object Tasks: Table("tasks") {
    val id = varchar("id", 64)
    val dayId = varchar("day_id", 64).references(Days.id)
    val title = text("title")
    val isCompleted = bool("is_completed").default(false)
    override val primaryKey = PrimaryKey(id)
}

object Notes: Table("notes") {
    val id = varchar("id", 64)
    val dayId = varchar("day_id", 64).references(Days.id)
    val content = text("content")
    override val primaryKey = PrimaryKey(id)
}
