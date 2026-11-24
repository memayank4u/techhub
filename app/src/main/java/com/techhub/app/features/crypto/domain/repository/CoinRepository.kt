package com.techhub.app.features.crypto.domain.repository

import com.techhub.app.core.common.Resource
import com.techhub.app.features.crypto.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(forceRefresh: Boolean = false): Flow<Resource<List<Coin>>>
    fun searchCoins(query: String): Flow<List<Coin>>
    suspend fun getCoinById(coinId: String): Coin?
}
