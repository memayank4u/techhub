package com.techhub.app.features.articles.domain.usecase

import com.techhub.app.core.common.Resource
import com.techhub.app.features.articles.domain.model.Article
import com.techhub.app.features.articles.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(forceRefresh: Boolean = false): Flow<Resource<List<Article>>> {
        return repository.getArticles(forceRefresh = forceRefresh)
    }
}
