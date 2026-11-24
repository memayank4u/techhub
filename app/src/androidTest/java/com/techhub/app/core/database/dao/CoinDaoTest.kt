package com.techhub.app.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.techhub.app.core.database.TechHubDatabase
import com.techhub.app.core.database.entity.CoinEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinDaoTest {

    private lateinit var database: TechHubDatabase
    private lateinit var dao: CoinDao

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TechHubDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.coinDao
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetAllCoins() = runBlocking {
        val coin = CoinEntity(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            image = "https://example.com/btc.png",
            currentPrice = 90000.0,
            marketCap = 1800000000000L,
            marketCapRank = 1,
            totalVolume = 30000000000.0,
            high24h = 91000.0,
            low24h = 89000.0,
            priceChangePercentage24h = 2.0,
            sparklineIn7d = listOf(89000.0, 90000.0)
        )

        dao.insertCoins(listOf(coin))
        val result = dao.getAllCoins().first()

        assertEquals(1, result.size)
        assertEquals("Bitcoin", result.first().name)
    }

    @Test
    fun searchCoinsReturnsMatchingSymbols() = runBlocking {
        val btc = CoinEntity("btc", "BTC", "Bitcoin", "", 90000.0, 1L, 1, 0.0, 0.0, 0.0, 0.0, emptyList())
        val eth = CoinEntity("eth", "ETH", "Ethereum", "", 3400.0, 2L, 2, 0.0, 0.0, 0.0, 0.0, emptyList())

        dao.insertCoins(listOf(btc, eth))
        val searchBtc = dao.searchCoins("Bit").first()

        assertEquals(1, searchBtc.size)
        assertEquals("Bitcoin", searchBtc.first().name)
    }
}
