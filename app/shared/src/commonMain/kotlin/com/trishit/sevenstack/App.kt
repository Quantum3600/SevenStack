package com.trishit.sevenstack

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.trishit.sevenstack.ui.SevenStackTheme
import com.trishit.sevenstack.ui.screens.AuthScreen
import com.trishit.sevenstack.ui.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    SevenStackTheme(
        appTheme = settingsState.theme,
        colorPalette = settingsState.palette,
        appFont = settingsState.font
    ) {
        Navigator(screen = AuthScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }
}
