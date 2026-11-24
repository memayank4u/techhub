package com.techhub.app.features.crypto.domain.usecase

import com.techhub.app.core.common.Resource
import com.techhub.app.features.crypto.domain.model.Coin
import com.techhub.app.features.crypto.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(forceRefresh: Boolean = false): Flow<Resource<List<Coin>>> {
        return repository.getCoins(forceRefresh = forceRefresh)
    }
}
