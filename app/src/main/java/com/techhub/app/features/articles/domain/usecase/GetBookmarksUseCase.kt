package com.techhub.app.features.articles.domain.usecase

import com.techhub.app.features.articles.domain.model.Article
import com.techhub.app.features.articles.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(): Flow<List<Article>> {
        return repository.getBookmarkedArticles()
    }
}
