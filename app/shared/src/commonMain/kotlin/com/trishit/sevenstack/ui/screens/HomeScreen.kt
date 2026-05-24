package com.trishit.sevenstack.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.trishit.sevenstack.DayDto
import com.trishit.sevenstack.NoteDto
import com.trishit.sevenstack.TaskDto
import com.trishit.sevenstack.ui.SevenStackTheme
import com.trishit.sevenstack.ui.components.DaySection
import com.trishit.sevenstack.ui.models.AppFont
import com.trishit.sevenstack.ui.models.AppTheme
import com.trishit.sevenstack.ui.models.ColorPalette
import com.trishit.sevenstack.ui.viewmodel.AppUiState
import com.trishit.sevenstack.ui.viewmodel.AppViewModel
import com.trishit.sevenstack.ui.viewmodel.SettingsUiState
import com.trishit.sevenstack.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import sevenstack.app.shared.generated.resources.Res
import sevenstack.app.shared.generated.resources.back_icon
import sevenstack.app.shared.generated.resources.gear_svgrepo_com
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
            onLoadWeek = { viewModel.loadWeek(it) },
            onTaskToggled = { taskId, isCompleted ->
                viewModel.onTaskToggled(taskId, isCompleted)
            },
            onAddTask = { dayId, title ->
                viewModel.onAddTask(dayId, title)
            },
            onDeleteTask = { taskId ->
                viewModel.onDeleteTask(taskId)
            },
            onSaveNote = { dayId, rawText, noteId ->
                viewModel.onSaveNote(dayId, rawText, noteId)
            },
            onDeleteNote = { noteId ->
                viewModel.onDeleteNote(noteId)
            },
            onSettingsClick = { navigator.push(SettingsScreen()) }
        )
    }
}

@Composable
fun HomeScreenContent(
    uiState: AppUiState,
    settingsState: SettingsUiState,
    onLoadWeek: (Int) -> Unit,
    onTaskToggled: (String, Boolean) -> Unit,
    onAddTask: (String, String) -> Unit,
    onDeleteTask: (String) -> Unit,
    onSaveNote: (String, String, String?) -> Unit,
    onDeleteNote: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
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
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    val initialPage = 500
    val pagerState = rememberPagerState(initialPage = initialPage) { 1000 }
    val scope = rememberCoroutineScope()

    val currentOffset = pagerState.currentPage - initialPage

    LaunchedEffect(pagerState.currentPage) {
        onLoadWeek(currentOffset)
        onLoadWeek(currentOffset - 1)
        onLoadWeek(currentOffset + 1)
    }

    // Manage individual expansion states per week to avoid weird jumping

    Surface(
        modifier = modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            val screenHeight = maxHeight
            val baseItemHeight = screenHeight / 7f

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = true
            ) { page ->
                val offset = page - initialPage
                val weekDays = uiState.weeks[offset] ?: emptyList()
                val listState = rememberLazyListState()
                val density = LocalDensity.current

                var expandedId by remember {
                    mutableStateOf(if (offset == 0) today.toString() else null)
                }

                LaunchedEffect(expandedId) {
                    if (expandedId != null) {
                        val index = weekDays.indexOfFirst { it.date == expandedId }
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

                if (weekDays.isEmpty() && uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingIndicator(
                            polygons = LoadingIndicatorDefaults.IndeterminateIndicatorPolygons
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        userScrollEnabled = true
                    ) {
                        itemsIndexed(weekDays, key = { _, day -> day.id }) { idx, day ->
                            DaySection(
                                day = day,
                                index = idx,
                                isExpanded = expandedId == day.date,
                                isDark = isDark,
                                selectedFont = settingsState.font,
                                baseHeight = baseItemHeight,
                                onExpandClick = {
                                    expandedId = if (expandedId == day.date) null else day.date
                                },
                                onTaskToggled = onTaskToggled,
                                onAddTaskClicked = { title -> onAddTask(day.id, title) },
                                onDeleteTaskClicked = onDeleteTask,
                                onSaveNoteClicked = onSaveNote,
                                onDeleteNoteClicked = onDeleteNote
                            )
                        }
                    }
                }
            }

            // Overlay for Month, Year, Week Number
            val showOverlay by remember { derivedStateOf { pagerState.isScrollInProgress } }
            val overlayInfo = remember(pagerState.currentPage) {
                val offset = pagerState.currentPage - initialPage
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val monday = now.minus(now.dayOfWeek.ordinal, DateTimeUnit.DAY)
                    .plus(offset * 7, DateTimeUnit.DAY)

                val monthName = monday.month.name.lowercase().replaceFirstChar { it.uppercase() }
                val year = monday.year
                val weekNum = (monday.dayOfYear - 1) / 7 + 1

                "$monthName $year, Week $weekNum"
            }

            AnimatedVisibility(
                visible = showOverlay,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(24.dp),
                    tonalElevation = 8.dp
                ) {
                    Text(
                        text = overlayInfo,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Back to Today FAB
            AnimatedVisibility(
                visible = currentOffset != 0,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 80.dp, end = 16.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(initialPage)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialShapes.Pill.toShape()
                ) {
                    val rotation = if (currentOffset < 0) 180f else 0f
                    Icon(
                        painter = painterResource(Res.drawable.back_icon),
                        contentDescription = "Back to Today",
                        modifier = Modifier.size(24.dp).rotate(rotation)
                    )
                }
            }

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .size(40.dp)
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
                TaskDto("1", "1", "Finish the project", isCompleted = true),
                TaskDto("2", "1", "Write documentation", isCompleted = false)
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
                TaskDto("3", "2", "Buy groceries", isCompleted = false)
            )
        )
    )

    val uiState = AppUiState(weeks = mapOf(0 to fakeDays), isLoading = false)
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
            onLoadWeek = {},
            onTaskToggled = { _, _ -> },
            onAddTask = { _, _ -> },
            onDeleteTask = {},
            onSaveNote = { _, _, _ -> },
            onDeleteNote = {},
            onSettingsClick = {}
        )
    }
}
