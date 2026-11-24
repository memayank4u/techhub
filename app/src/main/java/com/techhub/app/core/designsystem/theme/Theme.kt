package com.techhub.app.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private fun createColorScheme(
    isDark: Boolean,
    primaryLight: Color,
    primaryContainerLight: Color,
    primaryDark: Color,
    primaryContainerDark: Color
): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = primaryDark,
            onPrimary = Color(0xFF00287D),
            primaryContainer = primaryContainerDark,
            onPrimaryContainer = Color(0xFFDCE1FF),
            background = BackgroundDark,
            onBackground = OnBackgroundDark,
            surface = SurfaceDark,
            onSurface = OnSurfaceDark,
            surfaceVariant = SurfaceVariantDark,
            onSurfaceVariant = OnSurfaceVariantDark
        )
    } else {
        lightColorScheme(
            primary = primaryLight,
            onPrimary = Color.White,
            primaryContainer = primaryContainerLight,
            onPrimaryContainer = Color(0xFF001550),
            background = BackgroundLight,
            onBackground = OnBackgroundLight,
            surface = SurfaceLight,
            onSurface = OnSurfaceLight,
            surfaceVariant = SurfaceVariantLight,
            onSurfaceVariant = OnSurfaceVariantLight
        )
    }
}

@Composable
fun TechHubTheme(
    palette: ColorPalette = ColorPalette.COBALT,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = when (palette) {
        ColorPalette.COBALT -> createColorScheme(
            isDark = isDark,
            primaryLight = CobaltPrimaryLight,
            primaryContainerLight = CobaltPrimaryContainerLight,
            primaryDark = CobaltPrimaryDark,
            primaryContainerDark = CobaltPrimaryContainerDark
        )
        ColorPalette.EMERALD -> createColorScheme(
            isDark = isDark,
            primaryLight = EmeraldPrimaryLight,
            primaryContainerLight = EmeraldPrimaryContainerLight,
            primaryDark = EmeraldPrimaryDark,
            primaryContainerDark = EmeraldPrimaryContainerDark
        )
        ColorPalette.AMETHYST -> createColorScheme(
            isDark = isDark,
            primaryLight = AmethystPrimaryLight,
            primaryContainerLight = AmethystPrimaryContainerLight,
            primaryDark = AmethystPrimaryDark,
            primaryContainerDark = AmethystPrimaryContainerDark
        )
    }

    CompositionLocalProvider(LocalDimensions provides AppDimensions()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object TechHubTheme {
    val dimens: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current
}
