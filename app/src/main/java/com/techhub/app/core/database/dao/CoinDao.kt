package com.techhub.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.techhub.app.core.database.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Query("SELECT * FROM coins ORDER BY marketCapRank ASC")
    fun getAllCoins(): Flow<List<CoinEntity>>

    @Query("SELECT * FROM coins WHERE id = :coinId LIMIT 1")
    suspend fun getCoinById(coinId: String): CoinEntity?

    @Query("SELECT * FROM coins WHERE name LIKE '%' || :query || '%' OR symbol LIKE '%' || :query || '%' ORDER BY marketCapRank ASC")
    fun searchCoins(query: String): Flow<List<CoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CoinEntity>)

    @Query("DELETE FROM coins")
    suspend fun clearCoins()

    @Transaction
    suspend fun clearAndInsertCoins(coins: List<CoinEntity>) {
        clearCoins()
        insertCoins(coins)
    }
}
