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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.RoundedPolygon
import androidx.room.Index
import com.trishit.sevenstack.DayDto
import com.trishit.sevenstack.ui.getTypography
import com.trishit.sevenstack.ui.models.AppFont
import com.trishit.sevenstack.ui.models.ColorPalette
import org.jetbrains.compose.resources.painterResource
import sevenstack.app.shared.generated.resources.Res
import sevenstack.app.shared.generated.resources.rounded_event_list_24
import sevenstack.app.shared.generated.resources.rounded_stylus_note_24

private enum class DayViewMode { TASKS, NOTES }

@Composable
fun DaySection(
    day: DayDto,
    index: Int,
    isExpanded: Boolean,
    isDark: Boolean,
    selectedFont: AppFont,
    selectedPalette: ColorPalette,
    onExpandClick: () -> Unit,
    onTaskToggled: (String, Boolean) -> Unit,
    onAddTaskClicked: (String) -> Unit,
    onSaveNoteClicked: (String) -> Unit
) {
    val stepFactor = (index % 7) * 0.05
    val cardColor = remember(
        selectedPalette,
        isDark,
        index
    ) {
        if (isDark) {
            when (selectedPalette) {
                ColorPalette.MONOCHROME -> Color(0xFF1C1C1E).copy(alpha = (1f - stepFactor).toFloat())
                ColorPalette.RED -> Color(0xFF3A1212).copy(alpha = (1f - stepFactor).toFloat())
                ColorPalette.YELLOW -> Color(0xFF2D2A15).copy(alpha = (1f - stepFactor).toFloat())
                ColorPalette.GREEN -> Color(0xFF122A1C).copy(alpha = (1f - stepFactor).toFloat())
                ColorPalette.BLUE -> Color(0xFF121F3A).copy(alpha = (1f - stepFactor).toFloat())
            }
        } else {
            when (selectedPalette) {
                ColorPalette.MONOCHROME -> Color(0xFFE5E5EA).copy(alpha = (1f - stepFactor).toFloat())
                ColorPalette.RED -> Color(0xFFFFE5E5).copy(alpha = (1f - stepFactor).toFloat())
                ColorPalette.YELLOW -> Color(0xFFFFF9E5).copy(alpha = (1f - stepFactor).toFloat())
                ColorPalette.GREEN -> Color(0xFFE5FFE9).copy(alpha = (1f - stepFactor).toFloat())
                ColorPalette.BLUE -> Color(0xFFE5F1FF).copy(alpha = (1f - stepFactor).toFloat())
            }
        }
    }
    val contentColor = MaterialTheme.colorScheme.onPrimary
    var viewMode by remember { mutableStateOf(DayViewMode.TASKS) }
    var localNoteText by remember(day.notes) {
        mutableStateOf(day.notes.firstOrNull()?.content ?: "")
    }
    val animatedHeight by animateDpAsState(
        targetValue = if (isExpanded) 360.dp else 96.dp,
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
            .clickable { onExpandClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        style = getTypography(selectedFont).displaySmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = day.date.uppercase(),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    style = getTypography(selectedFont).displayLargeEmphasized,
                    color = contentColor,
                    modifier = Modifier.weight(1f)
                )
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
                                onCheckedChange = { isChecked -> onTaskToggled(task.id, isChecked) }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "+ Add new Task",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { onAddTaskClicked("New Task Block") }
                                .padding(vertical = 4.dp)
                        )
                    } else {
                        // Editorial Note View Panel
                        Column(modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                value = localNoteText,
                                onValueChange = {
                                    localNoteText = it
                                    onSaveNoteClicked(it) // Reactive write streaming directly back down to DB
                                },
                                placeholder = {
                                    if (localNoteText.isEmpty()) {
                                        Text(
                                            "Tap to add new Note",
                                            fontSize = 15.sp,
                                            color = contentColor.copy(alpha = 0.3f)
                                        )
                                    }
                                },
                                textStyle = getTypography(selectedFont).bodyLarge.copy(
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
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (viewMode == DayViewMode.TASKS) {
                            Res.drawable.rounded_event_list_24
                        } else {
                            Res.drawable.rounded_stylus_note_24
                        }
                    ),
                    contentDescription = "Switch Mode",
                    tint = contentColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}


