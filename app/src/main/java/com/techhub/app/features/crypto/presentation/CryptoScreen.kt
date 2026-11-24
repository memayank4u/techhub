package com.techhub.app.features.crypto.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.techhub.app.R
import com.techhub.app.core.designsystem.components.EmptyStateView
import com.techhub.app.core.designsystem.components.LoadingView
import com.techhub.app.core.designsystem.components.OfflineBanner
import com.techhub.app.core.designsystem.components.TechHubTopBar
import com.techhub.app.core.designsystem.theme.MarketGreen
import com.techhub.app.core.designsystem.theme.MarketGreenContainer
import com.techhub.app.core.designsystem.theme.MarketRed
import com.techhub.app.core.designsystem.theme.MarketRedContainer
import com.techhub.app.core.designsystem.theme.TechHubTheme
import com.techhub.app.features.crypto.domain.model.Coin

@Composable
fun CryptoScreen(
    viewModel: CryptoViewModel = hiltViewModel(),
    onOpenThemeSettings: () -> Unit = {},
    onCoinClick: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TechHubTopBar(
                title = stringResource(id = R.string.crypto_title),
                actions = {
                    IconButton(onClick = onOpenThemeSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = stringResource(id = R.string.theme_settings_tooltip),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { viewModel.onEvent(CryptoUiEvent.Refresh) }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.common_refresh),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Offline Warning Banner
            OfflineBanner(
                visible = state.isOffline,
                message = state.errorMessage ?: stringResource(id = R.string.common_offline_banner),
                onRetry = { viewModel.onEvent(CryptoUiEvent.Refresh) }
            )

            // Dynamic Hero Card
            MarketHeroCard(coins = state.coins)

            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onEvent(CryptoUiEvent.OnSearchQueryChanged(it)) },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.crypto_search_hint),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onEvent(CryptoUiEvent.ClearSearch) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(id = R.string.common_clear)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(TechHubTheme.dimens.radiusLarge),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TechHubTheme.dimens.spacingMedium,
                        vertical = TechHubTheme.dimens.spacingSmall
                    )
            )

            // Body List
            when {
                state.isLoading && state.displayList.isEmpty() -> {
                    LoadingView()
                }
                state.displayList.isEmpty() -> {
                    EmptyStateView(
                        onActionClick = { viewModel.onEvent(CryptoUiEvent.Refresh) }
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingSmall),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            start = TechHubTheme.dimens.spacingMedium,
                            end = TechHubTheme.dimens.spacingMedium,
                            bottom = TechHubTheme.dimens.spacingLarge
                        )
                    ) {
                        items(
                            items = state.displayList,
                            key = { it.id }
                        ) { coin ->
                            CoinListItem(
                                coin = coin,
                                onClick = { onCoinClick(coin.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MarketHeroCard(
    coins: List<Coin>,
    modifier: Modifier = Modifier
) {
    val btc = coins.firstOrNull { it.symbol.equals("BTC", ignoreCase = true) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = TechHubTheme.dimens.spacingMedium,
                vertical = TechHubTheme.dimens.spacingSmall
            ),
        shape = RoundedCornerShape(TechHubTheme.dimens.radiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .padding(TechHubTheme.dimens.spacingLarge)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.hero_market_pulse),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = stringResource(id = R.string.hero_market_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                if (btc != null) {
                    Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingMedium))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BTC ${stringResource(id = R.string.crypto_price_usd_format, btc.currentPrice)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Surface(
                            shape = RoundedCornerShape(TechHubTheme.dimens.radiusSmall),
                            color = if (btc.isPositiveChange) MarketGreenContainer else MarketRedContainer,
                            modifier = Modifier.padding(TechHubTheme.dimens.spacingXXSmall)
                        ) {
                            Text(
                                text = stringResource(id = R.string.crypto_change_format, btc.priceChangePercentage24h),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (btc.isPositiveChange) MarketGreen else MarketRed,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CoinListItem(
    coin: Coin,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(TechHubTheme.dimens.radiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TechHubTheme.dimens.spacingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "${coin.rank}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(TechHubTheme.dimens.spacingSmall))

            // Logo
            AsyncImage(
                model = coin.imageUrl,
                contentDescription = coin.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(TechHubTheme.dimens.coinLogoSize)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(TechHubTheme.dimens.spacingMedium))

            // Name & Symbol
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coin.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = coin.symbol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Sparkline Graph Preview
            if (coin.sparklinePoints.isNotEmpty()) {
                MiniSparkline(
                    points = coin.sparklinePoints,
                    isPositive = coin.isPositiveChange,
                    modifier = Modifier
                        .size(width = 54.dp, height = 28.dp)
                        .padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(TechHubTheme.dimens.spacingSmall))

            // Price & 24h Change Pill
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(id = R.string.crypto_price_usd_format, coin.currentPrice),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Surface(
                    shape = RoundedCornerShape(TechHubTheme.dimens.radiusSmall),
                    color = if (coin.isPositiveChange) MarketGreenContainer else MarketRedContainer
                ) {
                    Text(
                        text = stringResource(id = R.string.crypto_change_format, coin.priceChangePercentage24h),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (coin.isPositiveChange) MarketGreen else MarketRed,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MiniSparkline(
    points: List<Double>,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    val lineColor = if (isPositive) MarketGreen else MarketRed

    Canvas(modifier = modifier) {
        if (points.size < 2) return@Canvas

        val min = points.minOrNull() ?: 0.0
        val max = points.maxOrNull() ?: 1.0
        val range = if (max - min == 0.0) 1.0 else max - min

        val path = Path()
        points.forEachIndexed { index, price ->
            val x = (index.toFloat() / (points.size - 1)) * size.width
            val normalizedY = ((price - min) / range).toFloat()
            val y = size.height - (normalizedY * size.height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
