package com.trishit.sevenstack

interface ApiContract {
    suspend fun getWeek(userId: String, startDate: String, endDate: String): Result<List<DayDto>>
    suspend fun syncTasks(tasks: List<TaskDto>): Result<Unit>
    suspend fun syncNotes(notes: List<NoteDto>): Result<Unit>
}