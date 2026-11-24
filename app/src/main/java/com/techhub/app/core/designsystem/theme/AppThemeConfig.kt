package com.techhub.app.core.designsystem.theme

enum class ColorPalette {
    COBALT,
    EMERALD,
    AMETHYST
}

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

data class AppThemeState(
    val palette: ColorPalette = ColorPalette.COBALT,
    val mode: ThemeMode = ThemeMode.SYSTEM
)
