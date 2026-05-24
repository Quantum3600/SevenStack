package com.trishit.sevenstack

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val profilePicUrl: String? = null
)

@Serializable
data class DayDto(
    val id: String,
    val userId: String,
    val date: String,
    val tasks: List<TaskDto> = emptyList(),
    val notes: List<NoteDto> = emptyList()
)

@Serializable
data class TaskDto(
    val id: String,
    val dayId: String,
    val title: String,
    val isCompleted: Boolean
)

@Serializable
data class NoteDto(
    val id: String,
    val dayId: String,
    val content: String
)
