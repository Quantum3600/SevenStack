package com.trishit.sevenstack.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trishit.sevenstack.TaskDto
import com.trishit.sevenstack.ui.screens.CustomCheckbox

@Composable
fun TaskItem(
    task: TaskDto,
    textColor: Color,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val accentColor = MaterialTheme.colorScheme.primary
    val displayAlpha = if (enabled) 1f else 0.5f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onCheckedChange(!task.isCompleted) }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomCheckbox(
            checked = task.isCompleted,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = task.title,
            fontSize = 17.sp,
            color = (if (task.isCompleted) accentColor else textColor).copy(alpha = displayAlpha),
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
            fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold
        )
    }
}