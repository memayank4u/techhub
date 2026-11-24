package com.techhub.app.features.articles.data.mapper

import com.techhub.app.core.database.entity.ArticleEntity
import com.techhub.app.features.articles.data.dto.ArticleDto
import com.techhub.app.features.articles.domain.model.Article

fun ArticleDto.toEntity(isBookmarked: Boolean = false): ArticleEntity {
    return ArticleEntity(
        id = id,
        title = title,
        description = description ?: "",
        url = url,
        coverImage = coverImage,
        readablePublishDate = readablePublishDate ?: "Recently",
        readingTimeMinutes = readingTimeMinutes ?: 1,
        tagList = tagList ?: emptyList(),
        authorName = user?.name ?: "Dev Community",
        authorProfileImage = user?.profileImage,
        isBookmarked = isBookmarked
    )
}

fun ArticleEntity.toDomain(): Article {
    return Article(
        id = id,
        title = title,
        description = description,
        webUrl = url,
        coverImageUrl = coverImage,
        publishedDate = readablePublishDate,
        readingTimeMinutes = readingTimeMinutes,
        tags = tagList,
        authorName = authorName,
        authorProfileImageUrl = authorProfileImage,
        isBookmarked = isBookmarked
    )
}
