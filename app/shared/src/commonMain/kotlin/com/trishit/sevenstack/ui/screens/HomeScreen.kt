package com.trishit.sevenstack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.trishit.sevenstack.ui.components.DaySection
import com.trishit.sevenstack.ui.models.AppTheme
import com.trishit.sevenstack.ui.viewmodel.AppViewModel
import com.trishit.sevenstack.ui.viewmodel.SettingsViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import sevenstack.app.shared.generated.resources.Res
import sevenstack.app.shared.generated.resources.gear_svgrepo_com


class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<AppViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val settingsViewModel = koinViewModel<SettingsViewModel>()
        val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
        var expandedDayId by remember { mutableStateOf<String?>(null) }

        val isDark = when (settingsState.theme) {
            AppTheme.SYSTEM -> isSystemInDarkTheme()
            AppTheme.DARK -> true
            AppTheme.LIGHT -> false
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    LoadingIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        polygons = LoadingIndicatorDefaults.IndeterminateIndicatorPolygons
                    )
                } else {
                    LazyColumn(Modifier.fillMaxSize()) {
                        itemsIndexed(uiState.days) { idx, day ->
                            DaySection(
                                day = day,
                                index = idx,
                                isExpanded = expandedDayId == day.id,
                                isDark = isDark,
                                selectedFont = settingsState.font,
                                selectedPalette = settingsState.palette,
                                onExpandClick = {
                                    expandedDayId = if (expandedDayId == day.id) null else day.id
                                },
                                onTaskToggled = { taskId, isCompleted ->
                                    viewModel.onTaskToggled(taskId, isCompleted)
                                },
                                onAddTaskClicked = { title ->
                                    viewModel.onAddTask(day.id, title)
                                },
                                onSaveNoteClicked = { rawText ->
                                    viewModel.onSaveNote(day.id, rawText)
                                }
                            )
                        }
                    }
                }
                val navigator = LocalNavigator.currentOrThrow
                IconButton(
                    onClick = { navigator.push(SettingsScreen()) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .size(56.dp)
                        .background(Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.gear_svgrepo_com),
                        contentDescription = "Settings",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

