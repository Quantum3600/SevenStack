package com.trishit.sevenstack.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.trishit.sevenstack.ui.SevenStackTheme
import com.trishit.sevenstack.ui.components.DaySection
import org.jetbrains.compose.resources.painterResource
import sevenstack.app.shared.generated.resources.Res
import sevenstack.app.shared.generated.resources.add_task_placeholder
import sevenstack.app.shared.generated.resources.friday
import sevenstack.app.shared.generated.resources.gear_svgrepo_com
import sevenstack.app.shared.generated.resources.monday
import sevenstack.app.shared.generated.resources.saturday
import sevenstack.app.shared.generated.resources.sunday
import sevenstack.app.shared.generated.resources.task_1
import sevenstack.app.shared.generated.resources.task_2
import sevenstack.app.shared.generated.resources.task_3
import sevenstack.app.shared.generated.resources.task_4
import sevenstack.app.shared.generated.resources.task_5
import sevenstack.app.shared.generated.resources.task_date_placeholder
import sevenstack.app.shared.generated.resources.thursday
import sevenstack.app.shared.generated.resources.tuesday
import sevenstack.app.shared.generated.resources.wednesday

class TasksScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Box(modifier = Modifier.fillMaxSize()) {
            TasksScreenContent()

            IconButton(
                onClick = { navigator.push(SettingsScreen()) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .size(56.dp)
                    .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(28.dp))
                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(28.dp))
            ) {
                Icon(
                    painter = painterResource(Res.drawable.gear_svgrepo_com),
                    contentDescription = "Settings",
                    modifier = Modifier.size(28.dp),
                    tint = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun TasksScreenContent(modifier: Modifier = Modifier) {
    var expandedDay by remember { mutableStateOf("MONDAY") }

    val days = listOf(
        DayInfo("MONDAY", Res.string.monday, Color(0xFFE0E0E0)),
        DayInfo("TUESDAY", Res.string.tuesday, Color(0xFFD6D6D6)),
        DayInfo("WEDNESDAY", Res.string.wednesday, Color(0xFFCCCCCC)),
        DayInfo("THURSDAY", Res.string.thursday, Color(0xFFC2C2C2)),
        DayInfo("FRIDAY", Res.string.friday, Color(0xFFB8B8B8)),
        DayInfo("SATURDAY", Res.string.saturday, Color(0xFFAEAEAE)),
        DayInfo("SUNDAY", Res.string.sunday, Color(0xFFA4A4A4))
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFE0E0E0)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(days) { day ->
                DaySection1(
                    dayInfo = day,
                    isExpanded = expandedDay == day.id,
                    onExpandClick = { expandedDay = day.id }
                )
            }
        }
    }
}

data class DayInfo(
    val id: String,
    val nameRes: StringResource,
    val backgroundColor: Color
)

@Composable
fun DaySection1(
    dayInfo: DayInfo,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(dayInfo.backgroundColor)
            .clickable { onExpandClick() }
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Text(
            text = stringResource(dayInfo.nameRes),
            fontSize = 44.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF333333),
            lineHeight = 48.sp
        )

        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = stringResource(Res.string.task_date_placeholder),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                TaskItem(text = stringResource(Res.string.task_1), isInitialChecked = true)
                TaskItem(text = stringResource(Res.string.task_2), isInitialChecked = false)
                TaskItem(text = stringResource(Res.string.task_3), isInitialChecked = false)
                TaskItem(text = stringResource(Res.string.task_4), isInitialChecked = false)
                TaskItem(text = stringResource(Res.string.task_5), isInitialChecked = false)

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(Res.string.add_task_placeholder),
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    text: String,
    isInitialChecked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var checked by remember { mutableStateOf(isInitialChecked) }
    val orangeColor = Color(0xFFFF5722)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomCheckbox(
            checked = checked,
            onCheckedChange = { checked = it },
            enabled = enabled
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 18.sp,
            color = if (checked) orangeColor.copy(alpha = if (enabled) 1f else 0.5f) else Color(0xFF333333).copy(alpha = if (enabled) 1f else 0.5f),
            textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
        )
    }
}

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val checkboxColor = if (checked) Color(0xFFFF5722) else Color.Gray
    val alpha = if (enabled) 1f else 0.5f

    Box(
        modifier = modifier
            .size(20.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (checked) checkboxColor.copy(alpha = alpha) else Color.Transparent)
            .border(2.dp, checkboxColor.copy(alpha = alpha), RoundedCornerShape(4.dp))
            .clickable(enabled = enabled) { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Canvas(modifier = Modifier.size(12.dp)) {
                val path = Path().apply {
                    moveTo(size.width * 0.15f, size.height * 0.5f)
                    lineTo(size.width * 0.45f, size.height * 0.8f)
                    lineTo(size.width * 0.85f, size.height * 0.2f)
                }
                drawPath(
                    path = path,
                    color = Color.White,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }
        }
    }
}


@Preview
@Composable
fun TasksScreenPreview() {
    SevenStackTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TasksScreenContent()
        }
    }
}
