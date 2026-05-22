package com.trishit.sevenstack.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.trishit.sevenstack.ui.models.ColorPalette

object AppColors {
    // Monochrome
    private val Gray10 = Color(0xFF1A1C1E)
    private val Gray20 = Color(0xFF2F3033)
    private val Gray90 = Color(0xFFE2E2E6)
    private val Gray95 = Color(0xFFF1F0F4)
    private val Gray99 = Color(0xFFFDFBFF)

    val MonochromeLight = lightColorScheme(
        primary = Color.Black,
        onPrimary = Color.White,
        secondary = Color.DarkGray,
        onSecondary = Color.White,
        background = Gray99,
        surface = Gray99,
        onBackground = Color.Black,
        onSurface = Color.Black,
    )

    val MonochromeDark = darkColorScheme(
        primary = Color.White,
        onPrimary = Color.Black,
        secondary = Color.LightGray,
        onSecondary = Color.Black,
        background = Color.Black,
        surface = Gray10,
        onBackground = Color.White,
        onSurface = Color.White,
    )

    // Red
    val RedLight = lightColorScheme(
        primary = Color(0xFFBA1A1A),
        onPrimary = Color.White,
        secondary = Color(0xFF775652),
        onSecondary = Color.White,
        background = Color(0xFFFFFBFF),
        surface = Color(0xFFFFFBFF),
        onBackground = Color(0xFF201A19),
        onSurface = Color(0xFF201A19),
    )

    val RedDark = darkColorScheme(
        primary = Color(0xFFFFB4AB),
        onPrimary = Color(0xFF690005),
        secondary = Color(0xFFE7BDB8),
        onSecondary = Color(0xFF442926),
        background = Color(0xFF201A19),
        surface = Color(0xFF201A19),
        onBackground = Color(0xFFEDE0DE),
        onSurface = Color(0xFFEDE0DE),
    )

    // Blue
    val BlueLight = lightColorScheme(
        primary = Color(0xFF0061A4),
        onPrimary = Color.White,
        secondary = Color(0xFF535F70),
        onSecondary = Color.White,
        background = Color(0xFFFDFBFF),
        surface = Color(0xFFFDFBFF),
        onBackground = Color(0xFF1A1C1E),
        onSurface = Color(0xFF1A1C1E),
    )

    val BlueDark = darkColorScheme(
        primary = Color(0xFF9ECAFF),
        onPrimary = Color(0xFF003258),
        secondary = Color(0xFFBBC7DB),
        onSecondary = Color(0xFF253140),
        background = Color(0xFF1A1C1E),
        surface = Color(0xFF1A1C1E),
        onBackground = Color(0xFFE2E2E6),
        onSurface = Color(0xFFE2E2E6),
    )

    // Yellow
    val YellowLight = lightColorScheme(
        primary = Color(0xFF6D5E00),
        onPrimary = Color.White,
        secondary = Color(0xFF665E40),
        onSecondary = Color.White,
        background = Color(0xFFFFFBFF),
        surface = Color(0xFFFFFBFF),
        onBackground = Color(0xFF1E1B16),
        onSurface = Color(0xFF1E1B16),
    )

    val YellowDark = darkColorScheme(
        primary = Color(0xFFE9C400),
        onPrimary = Color(0xFF393000),
        secondary = Color(0xFFD1C6A1),
        onSecondary = Color(0xFF363016),
        background = Color(0xFF1E1B16),
        surface = Color(0xFF1E1B16),
        onBackground = Color(0xFFE8E2D9),
        onSurface = Color(0xFFE8E2D9),
    )

    // Green
    val GreenLight = lightColorScheme(
        primary = Color(0xFF006E1C),
        onPrimary = Color.White,
        secondary = Color(0xFF52634F),
        onSecondary = Color.White,
        background = Color(0xFFFCFDF6),
        surface = Color(0xFFFCFDF6),
        onBackground = Color(0xFF1A1C18),
        onSurface = Color(0xFF1A1C18),
    )

    val GreenDark = darkColorScheme(
        primary = Color(0xFF76DE7A),
        onPrimary = Color(0xFF00390A),
        secondary = Color(0xFFB9CCB4),
        onSecondary = Color(0xFF243424),
        background = Color(0xFF1A1C18),
        surface = Color(0xFF1A1C18),
        onBackground = Color(0xFFE2E3DD),
        onSurface = Color(0xFFE2E3DD),
    )

    // Purple
    val PurpleLight = lightColorScheme(
        primary = Color(0xFF6750A4),
        onPrimary = Color.White,
        secondary = Color(0xFF625B71),
        onSecondary = Color.White,
        background = Color(0xFFFFFBFF),
        surface = Color(0xFFFFFBFF),
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
    )

    val PurpleDark = darkColorScheme(
        primary = Color(0xFFD0BCFF),
        onPrimary = Color(0xFF381E72),
        secondary = Color(0xFFCCC2DC),
        onSecondary = Color(0xFF332D41),
        background = Color(0xFF1C1B1F),
        surface = Color(0xFF1C1B1F),
        onBackground = Color(0xFFE6E1E5),
        onSurface = Color(0xFFE6E1E5),
    )

    // Orange
    val OrangeLight = lightColorScheme(
        primary = Color(0xFF8B5000),
        onPrimary = Color.White,
        secondary = Color(0xFF715D49),
        onSecondary = Color.White,
        background = Color(0xFFFFFBFF),
        surface = Color(0xFFFFFBFF),
        onBackground = Color(0xFF1F1B16),
        onSurface = Color(0xFF1F1B16),
    )

    val OrangeDark = darkColorScheme(
        primary = Color(0xFFFFB870),
        onPrimary = Color(0xFF4A2800),
        secondary = Color(0xFFDFBFAF),
        onSecondary = Color(0xFF3F2E1D),
        background = Color(0xFF1F1B16),
        surface = Color(0xFF1F1B16),
        onBackground = Color(0xFFEAE1D9),
        onSurface = Color(0xFFEAE1D9),
    )

    // Pink
    val PinkLight = lightColorScheme(
        primary = Color(0xFF984061),
        onPrimary = Color.White,
        secondary = Color(0xFF78596B),
        onSecondary = Color.White,
        background = Color(0xFFFFFBFF),
        surface = Color(0xFFFFFBFF),
        onBackground = Color(0xFF201A1B),
        onSurface = Color(0xFF201A1B),
    )

    val PinkDark = darkColorScheme(
        primary = Color(0xFFFFB1C8),
        onPrimary = Color(0xFF5E1133),
        secondary = Color(0xFFE8BDD2),
        onSecondary = Color(0xFF462939),
        background = Color(0xFF201A1B),
        surface = Color(0xFF201A1B),
        onBackground = Color(0xFFEAE0E1),
        onSurface = Color(0xFFEAE0E1),
    )

    @Composable
    fun getColorScheme(palette: ColorPalette, isDark: Boolean): ColorScheme {
        return when (palette) {
            ColorPalette.DYNAMIC -> provideDynamicColorScheme(isDark) ?: (if (isDark) MonochromeDark else MonochromeLight)
            ColorPalette.MONOCHROME -> if (isDark) MonochromeDark else MonochromeLight
            ColorPalette.RED -> if (isDark) RedDark else RedLight
            ColorPalette.BLUE -> if (isDark) BlueDark else BlueLight
            ColorPalette.YELLOW -> if (isDark) YellowDark else YellowLight
            ColorPalette.GREEN -> if (isDark) GreenDark else GreenLight
            ColorPalette.PURPLE -> if (isDark) PurpleDark else PurpleLight
            ColorPalette.ORANGE -> if (isDark) OrangeDark else OrangeLight
            ColorPalette.PINK -> if (isDark) PinkDark else PinkLight
        }
    }
}
