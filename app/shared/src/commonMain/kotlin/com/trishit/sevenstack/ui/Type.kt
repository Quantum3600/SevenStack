package com.trishit.sevenstack.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.trishit.sevenstack.ui.models.AppFont

@Composable
fun getFontFamily(appFont: AppFont): FontFamily {
    return when (appFont) {
        AppFont.SYSTEM -> FontFamily.Default
        AppFont.BITCOUNT -> AppFonts.bitCount()
        AppFont.BOLDONSE -> AppFonts.boldonse()
        AppFont.BUNGEE -> AppFonts.bungee()
        AppFont.CLIMATE_CRISIS -> AppFonts.climateCrisis()
        AppFont.DYNAPUFF -> AppFonts.dyna_puff()
        AppFont.OI -> AppFonts.oi()
        AppFont.PRESS_START -> AppFonts.pressStart()
        AppFont.SARINA -> AppFonts.sarina()
        AppFont.SLACKEY -> AppFonts.slackey()
        AppFont.VINA_SANS -> AppFonts.vinaSans()
        AppFont.WELLFLEET -> AppFonts.wellFleet()
        AppFont.WORKBENCH -> AppFonts.workBench()
        AppFont.ZEN_DOTS -> AppFonts.zenDots()
    }
}

private fun getFontScale(font: AppFont): Float {
    return when (font) {
        AppFont.CLIMATE_CRISIS -> 0.50f
        AppFont.OI -> 0.5f
        AppFont.SARINA -> 0.6f
        AppFont.BITCOUNT -> 1.1f
        AppFont.DYNAPUFF -> 1.1f
        AppFont.VINA_SANS -> 1.25f
        AppFont.WORKBENCH -> 1.15f
        AppFont.PRESS_START -> 0.55f
        AppFont.BUNGEE -> 0.9f
        AppFont.ZEN_DOTS -> 0.5f
        else -> 0.8f
    }
}

private fun TextUnit.scaled(scale: Float): TextUnit {
    return if (isSp) (value * scale).sp else this
}

@Composable
fun getTypography(appFont: AppFont): Typography {
    val fontFamily = getFontFamily(appFont)
    val scale = getFontScale(appFont)

    return Typography(
        displayLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 57.sp.scaled(scale),
            lineHeight = 64.sp.scaled(scale),
            letterSpacing = (-0.25).sp,
        ),
        displayMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp.scaled(scale),
            lineHeight = 52.sp.scaled(scale),
            letterSpacing = 0.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp.scaled(scale),
            lineHeight = 44.sp.scaled(scale),
            letterSpacing = 0.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp.scaled(scale),
            lineHeight = 40.sp.scaled(scale),
            letterSpacing = 0.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp.scaled(scale),
            lineHeight = 36.sp.scaled(scale),
            letterSpacing = 0.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp.scaled(scale),
            lineHeight = 32.sp.scaled(scale),
            letterSpacing = 0.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp.scaled(scale),
            lineHeight = 28.sp.scaled(scale),
            letterSpacing = 0.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp.scaled(scale),
            lineHeight = 24.sp.scaled(scale),
            letterSpacing = 0.15.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp.scaled(scale),
            lineHeight = 20.sp.scaled(scale),
            letterSpacing = 0.1.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp.scaled(scale),
            lineHeight = 24.sp.scaled(scale),
            letterSpacing = 0.5.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp.scaled(scale),
            lineHeight = 20.sp.scaled(scale),
            letterSpacing = 0.25.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp.scaled(scale),
            lineHeight = 16.sp.scaled(scale),
            letterSpacing = 0.4.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp.scaled(scale),
            lineHeight = 20.sp.scaled(scale),
            letterSpacing = 0.1.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp.scaled(scale),
            lineHeight = 16.sp.scaled(scale),
            letterSpacing = 0.5.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp.scaled(scale),
            lineHeight = 16.sp.scaled(scale),
            letterSpacing = 0.5.sp
        )
    )
}
