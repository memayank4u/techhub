package com.techhub.app.features.articles.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.techhub.app.core.designsystem.theme.TechHubTheme
import com.techhub.app.features.articles.domain.model.Article

@Composable
fun ArticlesScreen(
    viewModel: ArticlesViewModel = hiltViewModel(),
    onOpenThemeSettings: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TechHubTopBar(
                title = stringResource(id = R.string.articles_title),
                actions = {
                    IconButton(onClick = onOpenThemeSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = stringResource(id = R.string.theme_settings_tooltip),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { viewModel.onEvent(ArticlesUiEvent.Refresh) }) {
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
            // Tab Selection (Latest vs Saved Bookmarks)
            TabRow(
                selectedTabIndex = state.selectedTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = state.selectedTab == ArticleTab.LATEST,
                    onClick = { viewModel.onEvent(ArticlesUiEvent.SelectTab(ArticleTab.LATEST)) },
                    text = {
                        Text(
                            text = stringResource(id = R.string.articles_tab_all),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (state.selectedTab == ArticleTab.LATEST) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = state.selectedTab == ArticleTab.BOOKMARKS,
                    onClick = { viewModel.onEvent(ArticlesUiEvent.SelectTab(ArticleTab.BOOKMARKS)) },
                    text = {
                        Text(
                            text = stringResource(id = R.string.articles_tab_saved),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (state.selectedTab == ArticleTab.BOOKMARKS) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            // Offline Banner
            OfflineBanner(
                visible = state.isOffline,
                message = state.errorMessage ?: stringResource(id = R.string.common_offline_banner),
                onRetry = { viewModel.onEvent(ArticlesUiEvent.Refresh) }
            )

            // Content
            when {
                state.isLoading && state.displayList.isEmpty() -> {
                    LoadingView()
                }
                state.displayList.isEmpty() -> {
                    if (state.selectedTab == ArticleTab.BOOKMARKS) {
                        EmptyStateView(
                            icon = Icons.Outlined.BookmarkBorder,
                            title = stringResource(id = R.string.articles_empty_bookmarks),
                            subtitle = stringResource(id = R.string.articles_empty_bookmarks_sub),
                            actionText = null
                        )
                    } else {
                        EmptyStateView(
                            onActionClick = { viewModel.onEvent(ArticlesUiEvent.Refresh) }
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = TechHubTheme.dimens.spacingMedium,
                            end = TechHubTheme.dimens.spacingMedium,
                            top = TechHubTheme.dimens.spacingSmall,
                            bottom = TechHubTheme.dimens.spacingLarge
                        ),
                        verticalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingMedium)
                    ) {
                        items(
                            items = state.displayList,
                            key = { it.id }
                        ) { article ->
                            ArticleCard(
                                article = article,
                                onBookmarkClick = {
                                    viewModel.onEvent(
                                        ArticlesUiEvent.ToggleBookmark(
                                            articleId = article.id,
                                            currentBookmarked = article.isBookmarked
                                        )
                                    )
                                },
                                onCardClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.webUrl))
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArticleCard(
    article: Article,
    onBookmarkClick: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(TechHubTheme.dimens.radiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Cover Image with Gradient Scrim
            if (!article.coverImageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(TechHubTheme.dimens.articleCoverHeight)
                ) {
                    AsyncImage(
                        model = article.coverImageUrl,
                        contentDescription = article.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.6f)
                                    )
                                )
                            )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TechHubTheme.dimens.spacingMedium)
            ) {
                // Author row & Bookmark icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!article.authorProfileImageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = article.authorProfileImageUrl,
                            contentDescription = article.authorName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(TechHubTheme.dimens.iconMedium)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(TechHubTheme.dimens.spacingSmall))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = article.authorName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = article.publishedDate,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    val bookmarkColor by animateColorAsState(
                        targetValue = if (article.isBookmarked) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        label = "BookmarkColor"
                    )

                    IconButton(onClick = onBookmarkClick) {
                        Icon(
                            imageVector = if (article.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = bookmarkColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingSmall))

                // Title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (article.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingXSmall))
                    Text(
                        text = article.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Tag Chips
                if (article.tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingSmall))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingXSmall),
                        verticalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingXXSmall)
                    ) {
                        article.tags.take(3).forEach { tag ->
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = "#$tag",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                shape = RoundedCornerShape(TechHubTheme.dimens.radiusSmall),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingSmall))

                // Reading time badge
                Surface(
                    shape = RoundedCornerShape(TechHubTheme.dimens.radiusSmall),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.articles_reading_time_format,
                            article.readingTimeMinutes
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
