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
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class AppRepository(private val dao: AppDao) {
    suspend fun initializeDays() {
        // Clear old format days if any (IDs "0".."6")
        val allDays = dao.getDaysInRange("0000-01-01", "9999-12-31")
        if (allDays.any { it.days.id.length < 5 }) {
            dao.deleteAllDays()
        }
        ensureWeekExists(0)
    }

    suspend fun ensureWeekExists(weekOffset: Int) {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val currentWeekMonday = now.minus(now.dayOfWeek.ordinal, DateTimeUnit.DAY)
        val targetMonday = currentWeekMonday.plus(weekOffset * 7, DateTimeUnit.DAY)
        val targetSunday = targetMonday.plus(6, DateTimeUnit.DAY)

        val existing = dao.getDaysInRange(targetMonday.toString(), targetSunday.toString())
        
        if (existing.size < 7) {
            val daysToInsert = (0..6).map { i ->
                val date = targetMonday.plus(i, DateTimeUnit.DAY)
                DayEntity(
                    id = date.toString(),
                    userId = "default",
                    date = date.toString()
                )
            }
            dao.insertDays(daysToInsert)
        }
    }

    fun observeWeek(weekOffset: Int): Flow<List<DayDto>> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val currentWeekMonday = now.minus(now.dayOfWeek.ordinal, DateTimeUnit.DAY)
        val targetMonday = currentWeekMonday.plus(weekOffset * 7, DateTimeUnit.DAY)
        val targetSunday = targetMonday.plus(6, DateTimeUnit.DAY)

        return dao.observeDaysInRange(targetMonday.toString(), targetSunday.toString()).map { list ->
            list.map { it.toDomainDto() }
        }
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