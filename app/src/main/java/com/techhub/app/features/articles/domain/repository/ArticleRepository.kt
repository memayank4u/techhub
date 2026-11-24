package com.techhub.app.features.articles.domain.repository

import com.techhub.app.core.common.Resource
import com.techhub.app.features.articles.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getArticles(forceRefresh: Boolean = false): Flow<Resource<List<Article>>>
    fun getBookmarkedArticles(): Flow<List<Article>>
    suspend fun toggleBookmark(articleId: Int, isBookmarked: Boolean)
}
