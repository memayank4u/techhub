package com.techhub.app.features.crypto.di

import com.techhub.app.features.crypto.data.repository.CoinRepositoryImpl
import com.techhub.app.features.crypto.domain.repository.CoinRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CryptoModule {

    @Binds
    @Singleton
    abstract fun bindCoinRepository(
        impl: CoinRepositoryImpl
    ): CoinRepository
}
