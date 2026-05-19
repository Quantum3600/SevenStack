package com.trishit.sevenstack.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trishit.sevenstack.DayDto
import com.trishit.sevenstack.NoteDto
import com.trishit.sevenstack.TaskDto
import com.trishit.sevenstack.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class AppUiState(
    val days: List<DayDto> = emptyList(),
    val isLoading: Boolean = true
)

class AppViewModel(private val repository: AppRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
    init {
        repository.observeApp().onEach { fresh ->
            _uiState.update {
                it.copy(
                    days = fresh,
                    isLoading = false
                )
            }
        }
            .launchIn(viewModelScope)
    }
    fun onTaskToggled(
        taskId: String,
        isCompleted: Boolean
    ) = viewModelScope.launch {
        repository.toggleTaskCompletion(
            taskId = taskId,
            isCompleted = isCompleted
        )
    }
    fun onAddTask(
        dayId: String,
        title: String
    ) = viewModelScope.launch {
        repository.addTask(
            TaskDto(
                id = Random.nextLong().toString(),
                dayId = dayId,
                title = title,
                isCompleted = false
            )
        )
    }
    fun onSaveNote(
        dayId: String,
        content: String
    ) = viewModelScope.launch {
        repository.saveNote(
            NoteDto(
                id = Random.nextLong().toString(),
                dayId = dayId,
                content = content
            )
        )
    }
    fun onDeleteTask(taskId: String) = viewModelScope.launch {
        repository.deleteTask(taskId)
    }

    fun onDeleteNote(noteId: String) = viewModelScope.launch {
        repository.deleteNote(noteId)
    }
}