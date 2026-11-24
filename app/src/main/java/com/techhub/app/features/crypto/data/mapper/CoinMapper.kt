package com.techhub.app.features.crypto.data.mapper

import com.techhub.app.core.database.entity.CoinEntity
import com.techhub.app.features.crypto.data.dto.CoinDto
import com.techhub.app.features.crypto.domain.model.Coin

fun CoinDto.toEntity(): CoinEntity {
    return CoinEntity(
        id = id,
        symbol = symbol.uppercase(),
        name = name,
        image = image,
        currentPrice = currentPrice ?: 0.0,
        marketCap = marketCap ?: 0L,
        marketCapRank = marketCapRank ?: 0,
        totalVolume = totalVolume ?: 0.0,
        high24h = high24h ?: 0.0,
        low24h = low24h ?: 0.0,
        priceChangePercentage24h = priceChangePercentage24h ?: 0.0,
        sparklineIn7d = sparklineIn7d?.price ?: emptyList()
    )
}

fun CoinEntity.toDomain(): Coin {
    return Coin(
        id = id,
        symbol = symbol,
        name = name,
        imageUrl = image,
        currentPrice = currentPrice,
        marketCap = marketCap,
        rank = marketCapRank,
        totalVolume = totalVolume,
        high24h = high24h,
        low24h = low24h,
        priceChangePercentage24h = priceChangePercentage24h,
        sparklinePoints = sparklineIn7d
    )
}
