package com.trishit.sevenstack.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.RoundedPolygon
import com.trishit.sevenstack.DayDto
import com.trishit.sevenstack.ui.getTypography
import com.trishit.sevenstack.ui.models.AppFont
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import sevenstack.app.shared.generated.resources.Res
import sevenstack.app.shared.generated.resources.rounded_event_list_24
import sevenstack.app.shared.generated.resources.rounded_stylus_note_24
import kotlin.time.Clock

private enum class DayViewMode { TASKS, NOTES }

@Composable
fun DaySection(
    day: DayDto,
    index: Int,
    isExpanded: Boolean,
    isDark: Boolean,
    selectedFont: AppFont,
    baseHeight: Dp,
    onExpandClick: () -> Unit,
    onTaskToggled: (String, Boolean) -> Unit,
    onAddTaskClicked: (String) -> Unit,
    onSaveNoteClicked: (String) -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val cardColor = remember(
        primaryColor,
        isDark,
        index
    ) {
        val alpha = (0.05f + (index % 7) * 0.05f).coerceAtMost(1f)
        if (isDark) {
            primaryColor.copy(alpha = alpha).compositeOver(Color(0xFF1C1C1E))
        } else {
            primaryColor.copy(alpha = alpha).compositeOver(Color(0xFFF5F5F7))
        }
    }
    val contentColor = MaterialTheme.colorScheme.onSurface
    val dateObj = remember(day.date) {
        try { LocalDate.parse(day.date) } catch (e: Exception) { null }
    }
    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
    val isPast = dateObj?.let { it < today } ?: false

    val dayName = dateObj?.dayOfWeek?.name ?: day.date

    var viewMode by remember { mutableStateOf(DayViewMode.TASKS) }
    var localNoteText by remember(day.id) {
        mutableStateOf(day.notes.firstOrNull()?.content ?: "")
    }
    var isAddingTask by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }

    var currentTime by remember {
        mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    }

    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            while (true) {
                currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                delay(60000)
            }
        }
    }

    val liveTimeStr = "${currentTime.hour.toString().padStart(2, '0')}:${currentTime.minute.toString().padStart(2, '0')}"

    val formattedCalendarDate = dateObj?.let {
        val monthName = it.month.name.lowercase().replaceFirstChar { char -> char.uppercase() }
        "$monthName ${it.day}, ${it.year} - $liveTimeStr"
    } ?: ""

    val animatedHeight by animateDpAsState(
        targetValue = if (isExpanded) 360.dp else baseHeight,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 200f
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = animatedHeight)
            .background(cardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(MaterialShapes.Gem.toShape())
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (index + 1).toString(),
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dayName.uppercase(),
                        fontWeight = FontWeight.Black,
                        style = getTypography(selectedFont).displayLargeEmphasized,
                        color = contentColor,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    AnimatedVisibility(visible = isExpanded) {
                        if (formattedCalendarDate.isNotEmpty()) {
                            Text(
                                text = formattedCalendarDate,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = contentColor.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = isExpanded
            ) {
                Column(modifier = Modifier.padding(top = 20.dp, start = 58.dp)) {
                    Text(
                        if (viewMode == DayViewMode.TASKS) "Your Tasks" else "Your Notes",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = contentColor.copy(alpha = 0.4f),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    if (viewMode == DayViewMode.TASKS) {
                        // Task View Panel
                        day.tasks.forEach { task ->
                            TaskItem(
                                task = task,
                                textColor = contentColor,
                                enabled = !isPast,
                                onCheckedChange = { isChecked ->
                                    if (!isPast) onTaskToggled(task.id, isChecked)
                                }
                            )
                        }

                        if (!isPast) {
                            Spacer(modifier = Modifier.height(16.dp))

                            if (isAddingTask) {
                                TextField(
                                    value = newTaskTitle,
                                    onValueChange = { newTaskTitle = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("Enter task title...") },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                    ),
                                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done,
                                        capitalization = KeyboardCapitalization.Sentences
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            if (newTaskTitle.isNotBlank()) {
                                                onAddTaskClicked(newTaskTitle)
                                                newTaskTitle = ""
                                                isAddingTask = false
                                            }
                                        }
                                    )
                                )
                            } else {
                                Text(
                                    text = "+ Add new Task",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .clickable { isAddingTask = true }
                                        .padding(vertical = 4.dp)
                                )
                            }
                        }
                    } else {
                        // Editorial Note View Panel
                        Column(modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                value = localNoteText,
                                onValueChange = {
                                    if (!isPast) {
                                        localNoteText = it
                                        onSaveNoteClicked(it)
                                    }
                                },
                                readOnly = isPast,
                                placeholder = {
                                    if (localNoteText.isEmpty()) {
                                        Text(
                                            if (isPast) "No notes for this day" else "Tap to add new Note",
                                            fontSize = 15.sp,
                                            color = contentColor.copy(alpha = 0.3f)
                                        )
                                    }
                                },
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = contentColor,
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 120.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    errorContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = isExpanded,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            IconButton(
                onClick = {
                    viewMode =
                        if (viewMode == DayViewMode.TASKS) DayViewMode.NOTES else DayViewMode.TASKS
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (viewMode == DayViewMode.NOTES) {
                            Res.drawable.rounded_event_list_24
                        } else {
                            Res.drawable.rounded_stylus_note_24
                        }
                    ),
                    contentDescription = "Switch Mode",
                    tint = contentColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


