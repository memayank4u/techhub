package com.techhub.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val marketCap: Long,
    val marketCapRank: Int,
    val totalVolume: Double,
    val high24h: Double,
    val low24h: Double,
    val priceChangePercentage24h: Double,
    val sparklineIn7d: List<Double>,
    val updatedAt: Long = System.currentTimeMillis()
)
