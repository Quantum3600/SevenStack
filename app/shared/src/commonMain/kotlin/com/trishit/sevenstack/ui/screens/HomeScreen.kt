package com.trishit.sevenstack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
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
import androidx.compose.ui.tooling.preview.Preview
import com.trishit.sevenstack.DayDto
import com.trishit.sevenstack.NoteDto
import com.trishit.sevenstack.TaskDto
import com.trishit.sevenstack.ui.SevenStackTheme
import com.trishit.sevenstack.ui.models.AppFont
import com.trishit.sevenstack.ui.models.ColorPalette
import com.trishit.sevenstack.ui.viewmodel.AppUiState
import com.trishit.sevenstack.ui.viewmodel.SettingsUiState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<AppViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val settingsViewModel = koinViewModel<SettingsViewModel>()
        val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.currentOrThrow

        HomeScreenContent(
            uiState = uiState,
            settingsState = settingsState,
            onTaskToggled = { taskId, isCompleted ->
                viewModel.onTaskToggled(taskId, isCompleted)
            },
            onAddTask = { dayId, title ->
                viewModel.onAddTask(dayId, title)
            },
            onSaveNote = { dayId, rawText ->
                viewModel.onSaveNote(dayId, rawText)
            },
            onSettingsClick = { navigator.push(SettingsScreen()) }
        )
    }
}

@Composable
fun HomeScreenContent(
    uiState: AppUiState,
    settingsState: SettingsUiState,
    onTaskToggled: (String, Boolean) -> Unit,
    onAddTask: (String, String) -> Unit,
    onSaveNote: (String, String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = when (settingsState.theme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = remember(primaryColor, isDark) {
        val base = if (isDark) Color(0xFF1C1C1E) else Color(0xFFF5F5F7)
        primaryColor.copy(alpha = 0.05f).compositeOver(base)
    }

    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
    }
    var expandedDayId by remember(uiState.days) {
        mutableStateOf(uiState.days.find { it.date == today }?.id)
    }

    val listState = rememberLazyListState()
    val density = LocalDensity.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            val screenHeight = maxHeight
            val baseItemHeight = screenHeight / 7f

            LaunchedEffect(expandedDayId) {
                if (expandedDayId != null) {
                    val index = uiState.days.indexOfFirst { it.id == expandedDayId }
                    if (index != -1) {
                        val targetOffsetPx = with(density) {
                            val itemHeightPx = 360.dp.toPx()
                            val viewportHeightPx = screenHeight.toPx()
                            ((viewportHeightPx - itemHeightPx) / 2).toInt().coerceAtLeast(0)
                        }
                        listState.animateScrollToItem(index, -targetOffsetPx)
                    }
                }
            }

            if (uiState.isLoading) {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    polygons = LoadingIndicatorDefaults.IndeterminateIndicatorPolygons
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(bottom = 0.dp),
                    userScrollEnabled = true
                ) {
                    itemsIndexed(uiState.days, key = { _, day -> day.id }) { idx, day ->
                        DaySection(
                            day = day,
                            index = idx,
                            isExpanded = expandedDayId == day.id,
                            isDark = isDark,
                            selectedFont = settingsState.font,
                            baseHeight = baseItemHeight,
                            onExpandClick = {
                                expandedDayId = if (expandedDayId == day.id) null else day.id
                            },
                            onTaskToggled = onTaskToggled,
                            onAddTaskClicked = { title -> onAddTask(day.id, title) },
                            onSaveNoteClicked = { rawText -> onSaveNote(day.id, rawText) }
                        )
                    }
                }
            }
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .size(40.dp)
                    .background(Color.Transparent)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.gear_svgrepo_com),
                    contentDescription = "Settings",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val fakeDays = listOf(
        DayDto(
            id = "1",
            userId = "user1",
            date = "2023-10-27",
            tasks = listOf(
                TaskDto("1", "1", "Finish the project", true),
                TaskDto("2", "1", "Write documentation", false)
            ),
            notes = listOf(
                NoteDto("1", "1", "Need to call the client")
            )
        ),
        DayDto(
            id = "2",
            userId = "user1",
            date = "2023-10-28",
            tasks = listOf(
                TaskDto("3", "2", "Buy groceries", false)
            )
        )
    )

    val uiState = AppUiState(days = fakeDays, isLoading = false)
    val settingsState = SettingsUiState(
        theme = AppTheme.LIGHT,
        palette = ColorPalette.MONOCHROME,
        font = AppFont.SYSTEM
    )

    SevenStackTheme(
        appTheme = settingsState.theme,
        colorPalette = settingsState.palette,
        appFont = settingsState.font
    ) {
        HomeScreenContent(
            uiState = uiState,
            settingsState = settingsState,
            onTaskToggled = { _, _ -> },
            onAddTask = { _, _ -> },
            onSaveNote = { _, _ -> },
            onSettingsClick = {}
        )
    }
}
