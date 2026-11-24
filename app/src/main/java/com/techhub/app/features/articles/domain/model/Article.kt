package com.techhub.app.features.articles.domain.model

data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val webUrl: String,
    val coverImageUrl: String?,
    val publishedDate: String,
    val readingTimeMinutes: Int,
    val tags: List<String>,
    val authorName: String,
    val authorProfileImageUrl: String?,
    val isBookmarked: Boolean = false
)
