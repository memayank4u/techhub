package com.techhub.app.core.designsystem.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("techhub_theme_prefs", Context.MODE_PRIVATE)

    private val _themeState = MutableStateFlow(loadSavedTheme())
    val themeState: StateFlow<AppThemeState> = _themeState.asStateFlow()

    fun updatePalette(palette: ColorPalette) {
        val updated = _themeState.value.copy(palette = palette)
        _themeState.value = updated
        prefs.edit().putString(KEY_PALETTE, palette.name).apply()
    }

    fun updateMode(mode: ThemeMode) {
        val updated = _themeState.value.copy(mode = mode)
        _themeState.value = updated
        prefs.edit().putString(KEY_MODE, mode.name).apply()
    }

    private fun loadSavedTheme(): AppThemeState {
        val savedPaletteName = prefs.getString(KEY_PALETTE, ColorPalette.COBALT.name)
        val savedModeName = prefs.getString(KEY_MODE, ThemeMode.SYSTEM.name)

        val palette = try {
            ColorPalette.valueOf(savedPaletteName ?: ColorPalette.COBALT.name)
        } catch (_: Exception) {
            ColorPalette.COBALT
        }

        val mode = try {
            ThemeMode.valueOf(savedModeName ?: ThemeMode.SYSTEM.name)
        } catch (_: Exception) {
            ThemeMode.SYSTEM
        }

        return AppThemeState(palette = palette, mode = mode)
    }

    companion object {
        private const val KEY_PALETTE = "pref_color_palette"
        private const val KEY_MODE = "pref_theme_mode"
    }
}
