package com.techhub.app.features.articles.data.repository

import com.techhub.app.core.common.DispatcherProvider
import com.techhub.app.core.common.Resource
import com.techhub.app.core.database.dao.ArticleDao
import com.techhub.app.core.network.api.DevToApi
import com.techhub.app.features.articles.data.mapper.toDomain
import com.techhub.app.features.articles.data.mapper.toEntity
import com.techhub.app.features.articles.domain.model.Article
import com.techhub.app.features.articles.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val api: DevToApi,
    private val dao: ArticleDao,
    private val dispatchers: DispatcherProvider
) : ArticleRepository {

    override fun getArticles(forceRefresh: Boolean): Flow<Resource<List<Article>>> = flow {
        // 1. Emit cached articles from Room immediately
        val cached = dao.getAllArticles().first().map { it.toDomain() }
        emit(Resource.Loading(data = cached))

        val shouldFetch = cached.isEmpty() || forceRefresh

        if (shouldFetch) {
            try {
                // 2. Fetch fresh articles from Dev.to API
                val dtos = api.getArticles()
                val entities = dtos.map { it.toEntity() }

                // 3. Save to Room while preserving any articles the user previously bookmarked
                dao.syncArticlesPreservingBookmarks(entities)

                // 4. Read back updated data
                val updated = dao.getAllArticles().first().map { it.toDomain() }
                emit(Resource.Success(data = updated))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = "Could not fetch new articles. Showing offline bookmarks and cache.",
                        data = cached,
                        cause = e
                    )
                )
            } catch (e: HttpException) {
                emit(
                    Resource.Error(
                        message = "Dev.to server returned error ${e.code()}",
                        data = cached,
                        cause = e
                    )
                )
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Failed to load articles",
                        data = cached,
                        cause = e
                    )
                )
            }
        } else {
            emit(Resource.Success(data = cached))
        }
    }.flowOn(dispatchers.io)

    override fun getBookmarkedArticles(): Flow<List<Article>> {
        return dao.getBookmarkedArticles().map { entities ->
            entities.map { it.toDomain() }
        }.flowOn(dispatchers.io)
    }

    override suspend fun toggleBookmark(articleId: Int, isBookmarked: Boolean) {
        dao.updateBookmarkStatus(articleId, isBookmarked)
    }
}
