package com.techhub.app.features.articles.data.mapper

import com.techhub.app.core.database.entity.ArticleEntity
import com.techhub.app.features.articles.data.dto.ArticleDto
import com.techhub.app.features.articles.data.dto.UserDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleMapperTest {

    @Test
    fun `ArticleDto toEntity maps correctly with bookmark status`() {
        val dto = ArticleDto(
            id = 42,
            title = "Kotlin 2.0 Released",
            description = "The new K2 compiler is here.",
            url = "https://dev.to/kotlin/2.0",
            coverImage = "https://example.com/cover.jpg",
            readablePublishDate = "Nov 20",
            readingTimeMinutes = 4,
            tagList = listOf("kotlin", "k2"),
            user = UserDto(name = "JetBrains", profileImage = "https://example.com/jb.png")
        )

        val entity = dto.toEntity(isBookmarked = true)

        assertEquals(42, entity.id)
        assertEquals("Kotlin 2.0 Released", entity.title)
        assertEquals("JetBrains", entity.authorName)
        assertTrue(entity.isBookmarked)
    }

    @Test
    fun `ArticleEntity toDomain maps correctly`() {
        val entity = ArticleEntity(
            id = 42,
            title = "Kotlin 2.0 Released",
            description = "The new K2 compiler is here.",
            url = "https://dev.to/kotlin/2.0",
            coverImage = "https://example.com/cover.jpg",
            readablePublishDate = "Nov 20",
            readingTimeMinutes = 4,
            tagList = listOf("kotlin", "k2"),
            authorName = "JetBrains",
            authorProfileImage = null,
            isBookmarked = true
        )

        val domain = entity.toDomain()

        assertEquals(42, domain.id)
        assertEquals("Kotlin 2.0 Released", domain.title)
        assertTrue(domain.isBookmarked)
        assertEquals(2, domain.tags.size)
    }
}
