package com.techhub.app.features.articles.presentation

import app.cash.turbine.test
import com.techhub.app.core.common.Resource
import com.techhub.app.features.articles.domain.model.Article
import com.techhub.app.features.articles.domain.usecase.GetArticlesUseCase
import com.techhub.app.features.articles.domain.usecase.GetBookmarksUseCase
import com.techhub.app.features.articles.domain.usecase.ToggleBookmarkUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getArticlesUseCase: GetArticlesUseCase = mockk()
    private val getBookmarksUseCase: GetBookmarksUseCase = mockk()
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk(relaxed = true)

    private val sampleArticle = Article(
        id = 101,
        title = "Clean Architecture in Jetpack Compose",
        description = "A guide to building maintainable apps.",
        webUrl = "https://dev.to/example/clean-architecture",
        coverImageUrl = "https://example.com/cover.png",
        publishedDate = "Nov 24",
        readingTimeMinutes = 5,
        tags = listOf("android", "kotlin", "compose"),
        authorName = "Mayank Patel",
        authorProfileImageUrl = null,
        isBookmarked = false
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getBookmarksUseCase() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadArticles successfully emits article list and updates UI state`() = runTest {
        // Given
        every { getArticlesUseCase(forceRefresh = false) } returns flowOf(
            Resource.Loading(),
            Resource.Success(listOf(sampleArticle))
        )

        // When
        val viewModel = ArticlesViewModel(
            getArticlesUseCase,
            getBookmarksUseCase,
            toggleBookmarkUseCase
        )

        // Then
        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(emptyList<Article>(), initial.articles)

            testDispatcher.scheduler.advanceUntilIdle()

            val successState = expectMostRecentItem()
            assertFalse(successState.isLoading)
            assertEquals(1, successState.articles.size)
            assertEquals("Clean Architecture in Jetpack Compose", successState.articles.first().title)
        }
    }

    @Test
    fun `SelectTab updates selectedTab in UI state`() = runTest {
        every { getArticlesUseCase(forceRefresh = false) } returns flowOf(Resource.Success(listOf(sampleArticle)))

        val viewModel = ArticlesViewModel(
            getArticlesUseCase,
            getBookmarksUseCase,
            toggleBookmarkUseCase
        )

        viewModel.onEvent(ArticlesUiEvent.SelectTab(ArticleTab.BOOKMARKS))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(ArticleTab.BOOKMARKS, state.selectedTab)
        }
    }

    @Test
    fun `ToggleBookmark calls ToggleBookmarkUseCase with inverted state`() = runTest {
        every { getArticlesUseCase(forceRefresh = false) } returns flowOf(Resource.Success(listOf(sampleArticle)))

        val viewModel = ArticlesViewModel(
            getArticlesUseCase,
            getBookmarksUseCase,
            toggleBookmarkUseCase
        )

        viewModel.onEvent(ArticlesUiEvent.ToggleBookmark(articleId = 101, currentBookmarked = false))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { toggleBookmarkUseCase(101, true) }
    }
}
