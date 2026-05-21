package com.trishit.sevenstack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trishit.sevenstack.repository.SettingsRepository
import com.trishit.sevenstack.ui.models.AppFont
import com.trishit.sevenstack.ui.models.AppTheme
import com.trishit.sevenstack.ui.models.ColorPalette
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val palette: ColorPalette = ColorPalette.MONOCHROME,
    var font: AppFont = AppFont.OI
)

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = combine(
        repository.themeFlow,
        repository.paletteFlow,
        repository.fontFlow
    ) { theme, palette, font ->
        SettingsUiState(theme, palette, font)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun onThemeSelected(theme: AppTheme) = viewModelScope.launch {
        repository.updateTheme(theme)
    }
    fun onPaletteSelected(palette: ColorPalette) = viewModelScope.launch {
        repository.updatePalette(palette)
    }
    fun onFontSelected(font: AppFont) = viewModelScope.launch {
        repository.updateFont(font)
    }
}
