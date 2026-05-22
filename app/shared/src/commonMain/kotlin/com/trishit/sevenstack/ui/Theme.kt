package com.trishit.sevenstack.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.trishit.sevenstack.ui.models.AppFont
import com.trishit.sevenstack.ui.models.AppTheme
import com.trishit.sevenstack.ui.models.ColorPalette

@Composable
expect fun provideDynamicColorScheme(isDark: Boolean): ColorScheme?

@Composable
fun SevenStackTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    colorPalette: ColorPalette = ColorPalette.MONOCHROME,
    appFont: AppFont = AppFont.SYSTEM,
    content: @Composable () -> Unit,
) {
    val isDark = when (appTheme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
    }

    val colorScheme = AppColors.getColorScheme(colorPalette, isDark)
    val typography = getTypography(AppFont.WELLFLEET)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
