package com.trishit.sevenstack.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.trishit.sevenstack.ui.models.AppFont
import com.trishit.sevenstack.ui.models.AppTheme
import com.trishit.sevenstack.ui.models.ColorPalette
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences> ) {
    private object Keys {
        val THEME = stringPreferencesKey("app_theme")
        val PALETTE = stringPreferencesKey("color_palette")
        val FONT = stringPreferencesKey("app_font")
    }

    val themeFlow: Flow<AppTheme> = dataStore.data.map { prefs ->
        prefs[Keys.THEME]?.let { AppTheme.valueOf(it) } ?: AppTheme.SYSTEM
    }

    val paletteFlow: Flow<ColorPalette> = dataStore.data.map { prefs ->
        prefs[Keys.PALETTE]?.let { ColorPalette.valueOf(it) } ?: ColorPalette.MONOCHROME
    }

    val fontFlow: Flow<AppFont> = dataStore.data.map { prefs ->
        prefs[Keys.FONT]?.let { AppFont.valueOf(it) } ?: AppFont.SYSTEM
    }

    suspend fun updateTheme(theme: AppTheme) {
        dataStore.edit { it[Keys.THEME] = theme.name }
    }

    suspend fun updatePalette(palette: ColorPalette) {
        dataStore.edit { it[Keys.PALETTE] = palette.name }
    }

    suspend fun updateFont(font: AppFont) {
        dataStore.edit { it[Keys.FONT] = font.name }
    }
}