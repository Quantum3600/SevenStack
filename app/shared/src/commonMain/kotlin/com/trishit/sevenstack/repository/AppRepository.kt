package com.trishit.sevenstack.repository

import com.trishit.sevenstack.DayDto
import com.trishit.sevenstack.NoteDto
import com.trishit.sevenstack.TaskDto
import com.trishit.sevenstack.database.AppDao
import com.trishit.sevenstack.database.DayWithContent
import com.trishit.sevenstack.database.NoteEntity
import com.trishit.sevenstack.database.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppRepository(private val dao: AppDao) {
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