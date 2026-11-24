package com.techhub.app.features.articles.presentation

sealed interface ArticlesUiEvent {
    data object Refresh : ArticlesUiEvent
    data class SelectTab(val tab: ArticleTab) : ArticlesUiEvent
    data class ToggleBookmark(val articleId: Int, val currentBookmarked: Boolean) : ArticlesUiEvent
}
