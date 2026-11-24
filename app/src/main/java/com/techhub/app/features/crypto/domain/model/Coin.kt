package com.techhub.app.features.crypto.domain.model

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String,
    val currentPrice: Double,
    val marketCap: Long,
    val rank: Int,
    val totalVolume: Double,
    val high24h: Double,
    val low24h: Double,
    val priceChangePercentage24h: Double,
    val sparklinePoints: List<Double>
) {
    val isPositiveChange: Boolean
        get() = priceChangePercentage24h >= 0
}
