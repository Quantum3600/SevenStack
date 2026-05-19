package com.trishit.sevenstack.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "days")
data class DayEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val date: String
)

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = DayEntity::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("dayId")]
)
data class TaskEntity(
    @PrimaryKey val id: String,
    val dayId: String,
    val title: String,
    val isCompleted: Boolean = false
)

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = DayEntity::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("dayId")]
)
data class NoteEntity(
    @PrimaryKey val id: String,
    val dayId: String,
    val content: String
)

data class DayWithContent(
    @Embedded val days: DayEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "dayId"
    ) val tasks: List<TaskEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "dayId"
    ) val notes: List<NoteEntity>
)

