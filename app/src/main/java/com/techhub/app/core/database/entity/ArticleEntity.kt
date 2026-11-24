package com.techhub.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val url: String,
    val coverImage: String?,
    val readablePublishDate: String,
    val readingTimeMinutes: Int,
    val tagList: List<String>,
    val authorName: String,
    val authorProfileImage: String?,
    val isBookmarked: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)
