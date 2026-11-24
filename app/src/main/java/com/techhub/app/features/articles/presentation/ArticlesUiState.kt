package com.techhub.app.features.articles.presentation

import com.techhub.app.features.articles.domain.model.Article

enum class ArticleTab {
    LATEST,
    BOOKMARKS
}

data class ArticlesUiState(
    val articles: List<Article> = emptyList(),
    val bookmarkedArticles: List<Article> = emptyList(),
    val selectedTab: ArticleTab = ArticleTab.LATEST,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isOffline: Boolean = false,
    val errorMessage: String? = null
) {
    val displayList: List<Article>
        get() = when (selectedTab) {
            ArticleTab.LATEST -> articles
            ArticleTab.BOOKMARKS -> bookmarkedArticles
        }
}
