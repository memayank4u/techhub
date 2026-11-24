package com.techhub.app.features.crypto.data.repository

import com.techhub.app.core.common.DispatcherProvider
import com.techhub.app.core.common.Resource
import com.techhub.app.core.database.dao.CoinDao
import com.techhub.app.core.network.api.CoinGeckoApi
import com.techhub.app.features.crypto.data.mapper.toDomain
import com.techhub.app.features.crypto.data.mapper.toEntity
import com.techhub.app.features.crypto.domain.model.Coin
import com.techhub.app.features.crypto.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi,
    private val dao: CoinDao,
    private val dispatchers: DispatcherProvider
) : CoinRepository {

    override fun getCoins(forceRefresh: Boolean): Flow<Resource<List<Coin>>> = flow {
        // 1. Immediately read cached coins from Room
        val cachedCoins = dao.getAllCoins().first().map { it.toDomain() }
        emit(Resource.Loading(data = cachedCoins))

        // 2. Decide if network sync is needed
        val shouldFetchFromNetwork = cachedCoins.isEmpty() || forceRefresh

        if (shouldFetchFromNetwork) {
            try {
                // 3. Network fetch
                val remoteDtos = api.getCoins()
                val entities = remoteDtos.map { it.toEntity() }

                // 4. Update Room DB (Single Source of Truth)
                dao.clearAndInsertCoins(entities)

                // 5. Read back fresh data from DB
                val updatedCoins = dao.getAllCoins().first().map { it.toDomain() }
                emit(Resource.Success(data = updatedCoins))
            } catch (e: IOException) {
                // Network unreachable -> Emit Error containing the cached data
                emit(
                    Resource.Error(
                        message = "Unable to connect to market server. Showing cached data.",
                        data = cachedCoins,
                        cause = e
                    )
                )
            } catch (e: HttpException) {
                // Server/HTTP error
                emit(
                    Resource.Error(
                        message = "Market server error (Code ${e.code()}). Showing cached data.",
                        data = cachedCoins,
                        cause = e
                    )
                )
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Unexpected error occurred.",
                        data = cachedCoins,
                        cause = e
                    )
                )
            }
        } else {
            // Already has cache and forceRefresh wasn't requested
            emit(Resource.Success(data = cachedCoins))
        }
    }.flowOn(dispatchers.io)

    override fun searchCoins(query: String): Flow<List<Coin>> {
        return dao.searchCoins(query).map { entities ->
            entities.map { it.toDomain() }
        }.flowOn(dispatchers.io)
    }

    override suspend fun getCoinById(coinId: String): Coin? {
        return dao.getCoinById(coinId)?.toDomain()
    }
}
