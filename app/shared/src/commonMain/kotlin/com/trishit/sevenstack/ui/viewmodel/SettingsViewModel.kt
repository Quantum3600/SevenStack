package com.trishit.sevenstack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trishit.sevenstack.UserDto
import com.trishit.sevenstack.auth.GoogleAuthEngine
import com.trishit.sevenstack.network.SevenStackApiClient
import com.trishit.sevenstack.repository.SettingsRepository
import com.trishit.sevenstack.ui.models.AppFont
import com.trishit.sevenstack.ui.models.AppTheme
import com.trishit.sevenstack.ui.models.ColorPalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: UserDto) : AuthState()
    data class Error(val message: String) : AuthState()
}

data class SettingsUiState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val palette: ColorPalette = ColorPalette.MONOCHROME,
    var font: AppFont = AppFont.OI,
    val authState: AuthState = AuthState.Unauthenticated
)

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val apiClient: SevenStackApiClient,
    private val authEngine: GoogleAuthEngine
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = combine(
        repository.themeFlow,
        repository.paletteFlow,
        repository.fontFlow,
        _uiState
    ) { theme, palette, font, currentUiState ->
        currentUiState.copy(theme = theme, palette = palette, font = font)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun triggerGoogleIdentityVerification() {
        viewModelScope.launch {
            _uiState.update { it.copy(authState = AuthState.Loading) }
            try {
                val idToken = authEngine.fetchGoogleIdToken()
                apiClient.verifyGoogleToken(idToken)
                    .onSuccess { user ->
                        _uiState.update { it.copy(authState = AuthState.Authenticated(user)) }
                    }
                    .onFailure { err ->
                        _uiState.update { it.copy(authState = AuthState.Error(err.message ?: "Verification failed")) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(authState = AuthState.Error(e.message ?: "Auth cancelled")) }
            }
        }
    }

    fun handleLogOut() = viewModelScope.launch {
        _uiState.update { it.copy(authState = AuthState.Loading) }
        try {
            authEngine.clearAuthSession()
            _uiState.update { it.copy(authState = AuthState.Unauthenticated) }
        } catch (e: Exception) {
            _uiState.update { it.copy(authState = AuthState.Unauthenticated) }
        }
    }

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
