package com.trishit.sevenstack.repository

import com.trishit.sevenstack.DayDto
import com.trishit.sevenstack.NoteDto
import com.trishit.sevenstack.TaskDto
import com.trishit.sevenstack.database.AppDao
import com.trishit.sevenstack.database.DayEntity
import com.trishit.sevenstack.database.DayWithContent
import com.trishit.sevenstack.database.NoteEntity
import com.trishit.sevenstack.database.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class AppRepository(private val dao: AppDao) {
    suspend fun initializeDays() {
        val existing = dao.observeAllDays().firstOrNull() ?: emptyList()
        // If the database has old string dates (like "Monday"), clear it once to fix.
        if (existing.isNotEmpty() && existing.any { !it.days.date.contains("-") }) {
            dao.deleteAllDays()
        }

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val offset = now.dayOfWeek.ordinal // Monday is 0, Sunday is 6
        val monday = now.minus(offset, DateTimeUnit.DAY)

        val days = (0..6).map { i ->
            monday.plus(i, DateTimeUnit.DAY)
        }

        dao.insertDays(days.mapIndexed { index, date ->
            DayEntity(
                id = index.toString(),
                userId = "default",
                date = date.toString()
            )
        })
    }

    fun observeApp(): Flow<List<DayDto>> = dao.observeAllDays().map { list ->
        list.map { it.toDomainDto() }
    }
    suspend fun toggleTaskCompletion(
        taskId: String,
        isCompleted: Boolean
    ) = dao.updateTaskStatus(taskId, isCompleted)
    suspend fun addTask(task: TaskDto) = dao.insertTask(
        TaskEntity(
            task.id,
            task.dayId,
            task.title,
            task.isCompleted
        )
    )
    suspend fun deleteTask(taskId: String) = dao.deleteTask(
        taskId
    )
    suspend fun saveNote(note: NoteDto) = dao.insertNote(
        NoteEntity(
            note.id,
            note.dayId,
            note.content
        )
    )
    suspend fun deleteNote(noteId: String) = dao.deleteNote(
        noteId
    )
}

private fun DayWithContent.toDomainDto() = DayDto(
    days.id,
    days.userId,
    days.date,
    tasks.map {
        TaskDto(
            it.id,
            it.dayId,
            it.title,
            it.isCompleted
        )
    },
    notes.map {
        NoteDto(
            it.id,
            it.dayId,
            it.content
        )
    }
)