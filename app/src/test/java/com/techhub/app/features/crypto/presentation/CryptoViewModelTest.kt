package com.techhub.app.features.crypto.presentation

import app.cash.turbine.test
import com.techhub.app.core.common.Resource
import com.techhub.app.features.crypto.domain.model.Coin
import com.techhub.app.features.crypto.domain.usecase.GetCoinsUseCase
import com.techhub.app.features.crypto.domain.usecase.SearchCoinsUseCase
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
class CryptoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getCoinsUseCase: GetCoinsUseCase = mockk()
    private val searchCoinsUseCase: SearchCoinsUseCase = mockk()

    private val fakeCoin = Coin(
        id = "bitcoin",
        symbol = "BTC",
        name = "Bitcoin",
        imageUrl = "https://example.com/btc.png",
        currentPrice = 65000.0,
        marketCap = 1200000000000L,
        rank = 1,
        totalVolume = 30000000000.0,
        high24h = 66000.0,
        low24h = 64000.0,
        priceChangePercentage24h = 2.5,
        sparklinePoints = listOf(64000.0, 64500.0, 65000.0)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCoins emits Loading and then Success with coins list`() = runTest {
        // Given
        every { getCoinsUseCase(forceRefresh = false) } returns flowOf(
            Resource.Loading(),
            Resource.Success(listOf(fakeCoin))
        )

        // When
        val viewModel = CryptoViewModel(getCoinsUseCase, searchCoinsUseCase)

        // Then
        viewModel.uiState.test {
            // Initial state
            val initialState = awaitItem()
            assertEquals(emptyList<Coin>(), initialState.coins)

            testDispatcher.scheduler.advanceUntilIdle()

            // Success state
            val successState = expectMostRecentItem()
            assertFalse(successState.isLoading)
            assertFalse(successState.isOffline)
            assertEquals(1, successState.coins.size)
            assertEquals("Bitcoin", successState.coins.first().name)
        }
    }

    @Test
    fun `loadCoins with network error sets isOffline true and preserves cached coins`() = runTest {
        // Given
        val cachedCoins = listOf(fakeCoin)
        every { getCoinsUseCase(forceRefresh = false) } returns flowOf(
            Resource.Error(
                message = "Unable to connect to server",
                data = cachedCoins
            )
        )

        // When
        val viewModel = CryptoViewModel(getCoinsUseCase, searchCoinsUseCase)

        // Then
        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val offlineState = expectMostRecentItem()
            assertFalse(offlineState.isLoading)
            assertEquals(true, offlineState.isOffline)
            assertEquals(1, offlineState.coins.size)
            assertEquals("Unable to connect to server", offlineState.errorMessage)
        }
    }
}
