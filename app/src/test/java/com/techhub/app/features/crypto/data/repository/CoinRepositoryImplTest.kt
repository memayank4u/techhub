package com.techhub.app.features.crypto.data.repository

import app.cash.turbine.test
import com.techhub.app.core.common.DispatcherProvider
import com.techhub.app.core.common.Resource
import com.techhub.app.core.database.dao.CoinDao
import com.techhub.app.core.database.entity.CoinEntity
import com.techhub.app.core.network.api.CoinGeckoApi
import com.techhub.app.features.crypto.data.dto.CoinDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CoinRepositoryImplTest {

    private val api: CoinGeckoApi = mockk()
    private val dao: CoinDao = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val dispatchers = object : DispatcherProvider {
        override val main = testDispatcher
        override val io = testDispatcher
        override val default = testDispatcher
    }

    private lateinit var repository: CoinRepositoryImpl

    private val cachedEntity = CoinEntity(
        id = "bitcoin",
        symbol = "BTC",
        name = "Bitcoin",
        image = "https://example.com/btc.png",
        currentPrice = 80000.0,
        marketCap = 1600000000000L,
        marketCapRank = 1,
        totalVolume = 30000000000.0,
        high24h = 81000.0,
        low24h = 79000.0,
        priceChangePercentage24h = 1.2,
        sparklineIn7d = emptyList()
    )

    @Before
    fun setUp() {
        repository = CoinRepositoryImpl(api, dao, dispatchers)
    }

    @Test
    fun `getCoins emits cached coins first then fresh coins on network success`() = runTest {
        // Given
        val remoteDto = CoinDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "https://example.com/btc.png",
            currentPrice = 85000.0
        )
        val updatedEntity = cachedEntity.copy(currentPrice = 85000.0)

        every { dao.getAllCoins() } returnsMany listOf(
            flowOf(listOf(cachedEntity)),
            flowOf(listOf(updatedEntity))
        )
        coEvery { api.getCoins() } returns listOf(remoteDto)

        // When & Then
        repository.getCoins(forceRefresh = true).test {
            val loading = awaitItem()
            assertTrue(loading is Resource.Loading)
            assertEquals(80000.0, loading.data?.first()?.currentPrice ?: 0.0, 0.001)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(85000.0, (success as Resource.Success).data.first().currentPrice, 0.001)

            awaitComplete()
        }

        coVerify(exactly = 1) { dao.clearAndInsertCoins(any()) }
    }

    @Test
    fun `getCoins emits Error with cached data when network fails`() = runTest {
        // Given
        every { dao.getAllCoins() } returns flowOf(listOf(cachedEntity))
        coEvery { api.getCoins() } throws IOException("No internet connection")

        // When & Then
        repository.getCoins(forceRefresh = true).test {
            val loading = awaitItem()
            assertTrue(loading is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(1, error.data?.size)
            assertEquals("Bitcoin", error.data?.first()?.name)

            awaitComplete()
        }
    }
}
