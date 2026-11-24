package com.techhub.app.features.crypto.domain.usecase

import app.cash.turbine.test
import com.techhub.app.core.common.Resource
import com.techhub.app.features.crypto.domain.model.Coin
import com.techhub.app.features.crypto.domain.repository.CoinRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCoinsUseCaseTest {

    private val repository: CoinRepository = mockk()
    private val useCase = GetCoinsUseCase(repository)

    private val sampleCoin = Coin(
        id = "ethereum",
        symbol = "ETH",
        name = "Ethereum",
        imageUrl = "https://example.com/eth.png",
        currentPrice = 3400.0,
        marketCap = 400000000000L,
        rank = 2,
        totalVolume = 15000000000.0,
        high24h = 3500.0,
        low24h = 3300.0,
        priceChangePercentage24h = 1.8,
        sparklinePoints = listOf(3300.0, 3350.0, 3400.0)
    )

    @Test
    fun `invoke with forceRefresh true calls repository getCoins with true`() = runTest {
        // Given
        every { repository.getCoins(forceRefresh = true) } returns flowOf(
            Resource.Success(listOf(sampleCoin))
        )

        // When
        useCase(forceRefresh = true).test {
            val item = awaitItem()
            assertTrue(item is Resource.Success)
            assertEquals(1, (item as Resource.Success).data.size)
            assertEquals("Ethereum", item.data.first().name)
            awaitComplete()
        }

        // Then
        verify(exactly = 1) { repository.getCoins(forceRefresh = true) }
    }
}
