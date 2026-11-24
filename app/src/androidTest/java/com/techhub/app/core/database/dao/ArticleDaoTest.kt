package com.techhub.app.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.techhub.app.core.database.TechHubDatabase
import com.techhub.app.core.database.entity.ArticleEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleDaoTest {

    private lateinit var database: TechHubDatabase
    private lateinit var dao: ArticleDao

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TechHubDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.articleDao
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun updateBookmarkStatusTogglesBookmark() = runBlocking {
        val article = ArticleEntity(
            id = 1,
            title = "Modern Android Testing",
            description = "Unit & Integration testing.",
            url = "https://example.com/test",
            coverImage = null,
            readablePublishDate = "Nov 24",
            readingTimeMinutes = 3,
            tagList = listOf("android"),
            authorName = "Mayank Patel",
            authorProfileImage = null,
            isBookmarked = false
        )

        dao.insertArticles(listOf(article))
        dao.updateBookmarkStatus(id = 1, isBookmarked = true)

        val bookmarked = dao.getBookmarkedArticles().first()
        assertEquals(1, bookmarked.size)
        assertTrue(bookmarked.first().isBookmarked)
    }

    @Test
    fun syncArticlesPreservingBookmarksKeepsBookmarkedState() = runBlocking {
        val original = ArticleEntity(
            id = 99,
            title = "Keep Me Bookmarked",
            description = "Test description",
            url = "https://example.com/keep",
            coverImage = null,
            readablePublishDate = "Nov 24",
            readingTimeMinutes = 2,
            tagList = emptyList(),
            authorName = "Mayank",
            authorProfileImage = null,
            isBookmarked = true
        )
        dao.insertArticles(listOf(original))

        // Remote sync arrives where isBookmarked is false from API
        val remoteSynced = original.copy(title = "Updated Title From API", isBookmarked = false)
        dao.syncArticlesPreservingBookmarks(listOf(remoteSynced))

        val result = dao.getArticleById(99)
        assertTrue(result != null)
        assertTrue(result!!.isBookmarked) // Preserved!
        assertEquals("Updated Title From API", result.title)
    }
}
