package com.trishit.sevenstack.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import sevenstack.app.shared.generated.resources.Res
import sevenstack.app.shared.generated.resources.bitcount_var
import sevenstack.app.shared.generated.resources.boldonse_regular
import sevenstack.app.shared.generated.resources.bungee_regular
import sevenstack.app.shared.generated.resources.climate_crisis_regular_var
import sevenstack.app.shared.generated.resources.dyna_puff_var
import sevenstack.app.shared.generated.resources.oi_regular
import sevenstack.app.shared.generated.resources.press_start_regular
import sevenstack.app.shared.generated.resources.sarina_regular
import sevenstack.app.shared.generated.resources.slackey_regular
import sevenstack.app.shared.generated.resources.vinasans_regular
import sevenstack.app.shared.generated.resources.wellfleet_regular
import sevenstack.app.shared.generated.resources.workbench_regular_var
import sevenstack.app.shared.generated.resources.zen_dots_regular

object AppFonts {
    @Composable
    fun bitCount() = FontFamily(
        Font(Res.font.bitcount_var)
    )
    @Composable
    fun boldonse() = FontFamily(
        Font(Res.font.boldonse_regular)
    )
    @Composable
    fun bungee() = FontFamily(
        Font(Res.font.bungee_regular)
    )
    @Composable
    fun climateCrisis() = FontFamily(
        Font(Res.font.climate_crisis_regular_var)
    )
    @Composable
    fun dyna_puff() = FontFamily(
        Font(Res.font.dyna_puff_var)
    )
    @Composable
    fun oi() = FontFamily(
        Font(Res.font.oi_regular)
    )
    @Composable
    fun pressStart() = FontFamily(
        Font(Res.font.press_start_regular)
    )
    @Composable
    fun sarina() = FontFamily(
        Font(Res.font.sarina_regular)
    )
    @Composable
    fun slackey() = FontFamily(
        Font(Res.font.slackey_regular)
    )
    @Composable
    fun vinaSans() = FontFamily(
        Font(Res.font.vinasans_regular)
    )
    @Composable
    fun wellFleet() = FontFamily(
        Font(Res.font.wellfleet_regular)
    )
    @Composable
    fun workBench() = FontFamily(
        Font(Res.font.workbench_regular_var)
    )
    @Composable
    fun zenDots() = FontFamily(
        Font(Res.font.zen_dots_regular)
    )
}