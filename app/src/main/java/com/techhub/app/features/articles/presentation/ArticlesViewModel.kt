package com.techhub.app.features.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techhub.app.core.common.Resource
import com.techhub.app.features.articles.domain.usecase.GetArticlesUseCase
import com.techhub.app.features.articles.domain.usecase.GetBookmarksUseCase
import com.techhub.app.features.articles.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArticlesUiState())
    val uiState: StateFlow<ArticlesUiState> = _uiState.asStateFlow()

    init {
        observeArticles(forceRefresh = false)
        observeBookmarks()
    }

    fun onEvent(event: ArticlesUiEvent) {
        when (event) {
            is ArticlesUiEvent.Refresh -> observeArticles(forceRefresh = true)
            is ArticlesUiEvent.SelectTab -> _uiState.update { it.copy(selectedTab = event.tab) }
            is ArticlesUiEvent.ToggleBookmark -> {
                viewModelScope.launch {
                    toggleBookmarkUseCase(event.articleId, !event.currentBookmarked)
                }
            }
        }
    }

    private fun observeArticles(forceRefresh: Boolean) {
        viewModelScope.launch {
            getArticlesUseCase(forceRefresh = forceRefresh).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { current ->
                            current.copy(
                                isLoading = current.articles.isEmpty(),
                                isRefreshing = forceRefresh,
                                articles = resource.data ?: current.articles,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update { current ->
                            current.copy(
                                isLoading = false,
                                isRefreshing = false,
                                isOffline = false,
                                articles = resource.data,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { current ->
                            current.copy(
                                isLoading = false,
                                isRefreshing = false,
                                isOffline = true,
                                articles = resource.data ?: current.articles,
                                errorMessage = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun observeBookmarks() {
        getBookmarksUseCase()
            .onEach { bookmarked ->
                _uiState.update { it.copy(bookmarkedArticles = bookmarked) }
            }
            .launchIn(viewModelScope)
    }
}
