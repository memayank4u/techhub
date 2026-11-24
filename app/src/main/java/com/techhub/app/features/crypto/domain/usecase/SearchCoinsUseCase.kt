package com.techhub.app.features.crypto.domain.usecase

import com.techhub.app.features.crypto.domain.model.Coin
import com.techhub.app.features.crypto.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(query: String): Flow<List<Coin>> {
        return repository.searchCoins(query.trim())
    }
}
