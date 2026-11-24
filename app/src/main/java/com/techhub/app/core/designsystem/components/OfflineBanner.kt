package com.techhub.app.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.techhub.app.R
import com.techhub.app.core.designsystem.theme.OfflineAmber
import com.techhub.app.core.designsystem.theme.OfflineAmberBackground
import com.techhub.app.core.designsystem.theme.OnOfflineAmber
import com.techhub.app.core.designsystem.theme.TechHubTheme

@Composable
fun OfflineBanner(
    visible: Boolean,
    modifier: Modifier = Modifier,
    message: String = stringResource(id = R.string.common_offline_banner),
    onRetry: (() -> Unit)? = null
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(OfflineAmberBackground)
                .padding(
                    horizontal = TechHubTheme.dimens.spacingMedium,
                    vertical = TechHubTheme.dimens.spacingSmall
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingSmall),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudOff,
                    contentDescription = null,
                    tint = OfflineAmber,
                    modifier = Modifier.size(TechHubTheme.dimens.iconMedium)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnOfflineAmber
                )
            }
            if (onRetry != null) {
                TextButton(onClick = onRetry) {
                    Text(
                        text = stringResource(id = R.string.common_retry),
                        color = OnOfflineAmber,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
