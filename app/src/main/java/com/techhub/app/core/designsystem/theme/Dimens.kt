package com.techhub.app.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimensions(
    val spacingXXSmall: Dp = 2.dp,
    val spacingXSmall: Dp = 4.dp,
    val spacingSmall: Dp = 8.dp,
    val spacingMedium: Dp = 16.dp,
    val spacingLarge: Dp = 24.dp,
    val spacingXLarge: Dp = 32.dp,

    val radiusSmall: Dp = 8.dp,
    val radiusMedium: Dp = 12.dp,
    val radiusLarge: Dp = 16.dp,
    val radiusXLarge: Dp = 24.dp,

    val elevationNone: Dp = 0.dp,
    val elevationLow: Dp = 2.dp,
    val elevationMedium: Dp = 4.dp,
    val elevationHigh: Dp = 8.dp,

    val iconSmall: Dp = 16.dp,
    val iconMedium: Dp = 24.dp,
    val iconLarge: Dp = 32.dp,
    val coinLogoSize: Dp = 40.dp,
    val articleCoverHeight: Dp = 160.dp
)

val LocalDimensions = staticCompositionLocalOf { AppDimensions() }
