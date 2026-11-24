package com.techhub.app.features.articles.domain.usecase

import com.techhub.app.features.articles.domain.repository.ArticleRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(articleId: Int, isBookmarked: Boolean) {
        repository.toggleBookmark(articleId, isBookmarked)
    }
}
