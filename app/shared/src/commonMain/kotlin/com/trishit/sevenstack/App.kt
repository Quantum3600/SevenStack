package com.trishit.sevenstack

import androidx.compose.runtime.*
import org.koin.compose.KoinContext
import androidx.compose.material3.Text
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import com.trishit.sevenstack.ui.SevenStackTheme
import com.trishit.sevenstack.ui.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinContext {
        val settingsViewModel = koinViewModel<SettingsViewModel>()
        val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
        SevenStackTheme(
            appTheme = settingsState.theme,
            colorPalette = settingsState.palette,
            appFont = settingsState.font
        ) {
            Navigator(screen = HomeScreen())
        }
    }
}
