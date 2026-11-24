package com.techhub.app.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.techhub.app.R
import com.techhub.app.core.designsystem.theme.TechHubTheme

@Composable
fun EmptyStateView(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Inbox,
    title: String = stringResource(id = R.string.common_empty_title),
    subtitle: String = stringResource(id = R.string.common_empty_subtitle),
    actionText: String? = stringResource(id = R.string.common_retry),
    onActionClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(TechHubTheme.dimens.spacingLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(TechHubTheme.dimens.spacingXLarge * 2)
        )
        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingMedium))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingSmall))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingLarge))
            Button(onClick = onActionClick) {
                Text(text = actionText)
            }
        }
    }
}
