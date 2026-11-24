package com.techhub.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.techhub.app.core.database.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles ORDER BY cachedAt DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE isBookmarked = 1 ORDER BY cachedAt DESC")
    fun getBookmarkedArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    suspend fun getArticleById(id: Int): ArticleEntity?

    @Query("UPDATE articles SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmarkStatus(id: Int, isBookmarked: Boolean)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT id FROM articles WHERE isBookmarked = 1")
    suspend fun getBookmarkedIds(): List<Int>

    @Transaction
    suspend fun syncArticlesPreservingBookmarks(remoteArticles: List<ArticleEntity>) {
        val bookmarkedIds = getBookmarkedIds().toSet()
        val merged = remoteArticles.map { article ->
            if (bookmarkedIds.contains(article.id)) {
                article.copy(isBookmarked = true)
            } else {
                article
            }
        }
        insertArticles(merged)
    }
}
