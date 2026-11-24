package com.techhub.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.app.R
import com.techhub.app.core.designsystem.theme.AmethystPrimaryLight
import com.techhub.app.core.designsystem.theme.AppThemeState
import com.techhub.app.core.designsystem.theme.CobaltPrimaryLight
import com.techhub.app.core.designsystem.theme.ColorPalette
import com.techhub.app.core.designsystem.theme.EmeraldPrimaryLight
import com.techhub.app.core.designsystem.theme.TechHubTheme
import com.techhub.app.core.designsystem.theme.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectorSheet(
    themeState: AppThemeState,
    onPaletteSelect: (ColorPalette) -> Unit,
    onModeSelect: (ThemeMode) -> Unit,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = TechHubTheme.dimens.spacingLarge,
                    end = TechHubTheme.dimens.spacingLarge,
                    bottom = TechHubTheme.dimens.spacingXLarge
                )
        ) {
            Text(
                text = stringResource(id = R.string.theme_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingMedium))

            // Color Palette Section
            Text(
                text = stringResource(id = R.string.theme_palette_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingMedium)
            ) {
                PaletteCard(
                    title = stringResource(id = R.string.theme_palette_cobalt),
                    color = CobaltPrimaryLight,
                    isSelected = themeState.palette == ColorPalette.COBALT,
                    onClick = { onPaletteSelect(ColorPalette.COBALT) },
                    modifier = Modifier.weight(1f)
                )
                PaletteCard(
                    title = stringResource(id = R.string.theme_palette_emerald),
                    color = EmeraldPrimaryLight,
                    isSelected = themeState.palette == ColorPalette.EMERALD,
                    onClick = { onPaletteSelect(ColorPalette.EMERALD) },
                    modifier = Modifier.weight(1f)
                )
                PaletteCard(
                    title = stringResource(id = R.string.theme_palette_amethyst),
                    color = AmethystPrimaryLight,
                    isSelected = themeState.palette == ColorPalette.AMETHYST,
                    onClick = { onPaletteSelect(ColorPalette.AMETHYST) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingLarge))

            // Theme Mode Section
            Text(
                text = stringResource(id = R.string.theme_mode_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingSmall)
            ) {
                FilterChip(
                    selected = themeState.mode == ThemeMode.SYSTEM,
                    onClick = { onModeSelect(ThemeMode.SYSTEM) },
                    label = { Text(text = stringResource(id = R.string.theme_mode_system)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                FilterChip(
                    selected = themeState.mode == ThemeMode.LIGHT,
                    onClick = { onModeSelect(ThemeMode.LIGHT) },
                    label = { Text(text = stringResource(id = R.string.theme_mode_light)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                FilterChip(
                    selected = themeState.mode == ThemeMode.DARK,
                    onClick = { onModeSelect(ThemeMode.DARK) },
                    label = { Text(text = stringResource(id = R.string.theme_mode_dark)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    }
}

@Composable
private fun PaletteCard(
    title: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(TechHubTheme.dimens.radiusMedium))
            .border(2.dp, borderColor, RoundedCornerShape(TechHubTheme.dimens.radiusMedium))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .clickable(onClick = onClick)
            .padding(TechHubTheme.dimens.spacingSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingXSmall))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
