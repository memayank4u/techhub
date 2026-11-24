package com.techhub.app.features.crypto.data.mapper

import com.techhub.app.core.database.entity.CoinEntity
import com.techhub.app.features.crypto.data.dto.CoinDto
import com.techhub.app.features.crypto.data.dto.SparklineDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CoinMapperTest {

    @Test
    fun `CoinDto toEntity maps all fields correctly`() {
        val dto = CoinDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "https://example.com/btc.png",
            currentPrice = 90000.0,
            marketCap = 1800000000000L,
            marketCapRank = 1,
            totalVolume = 45000000000.0,
            high24h = 91000.0,
            low24h = 88000.0,
            priceChangePercentage24h = 3.2,
            sparklineIn7d = SparklineDto(listOf(88000.0, 89000.0, 90000.0))
        )

        val entity = dto.toEntity()

        assertEquals("bitcoin", entity.id)
        assertEquals("BTC", entity.symbol) // Capitalized
        assertEquals("Bitcoin", entity.name)
        assertEquals(90000.0, entity.currentPrice, 0.001)
        assertEquals(1, entity.marketCapRank)
        assertEquals(3, entity.sparklineIn7d.size)
    }

    @Test
    fun `CoinEntity toDomain maps all fields correctly`() {
        val entity = CoinEntity(
            id = "solana",
            symbol = "SOL",
            name = "Solana",
            image = "https://example.com/sol.png",
            currentPrice = 220.0,
            marketCap = 100000000000L,
            marketCapRank = 4,
            totalVolume = 5000000000.0,
            high24h = 230.0,
            low24h = 210.0,
            priceChangePercentage24h = 5.0,
            sparklineIn7d = listOf(210.0, 215.0, 220.0)
        )

        val domain = entity.toDomain()

        assertEquals("solana", domain.id)
        assertEquals("SOL", domain.symbol)
        assertEquals(220.0, domain.currentPrice, 0.001)
        assertTrue(domain.isPositiveChange)
    }
}
