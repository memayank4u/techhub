package com.techhub.app.core.network.di

import com.google.ai.client.generativeai.GenerativeModel
import com.techhub.app.BuildConfig
import com.techhub.app.core.common.DefaultDispatcherProvider
import com.techhub.app.core.common.DispatcherProvider
import com.techhub.app.core.network.api.CoinGeckoApi
import com.techhub.app.core.network.api.DevToApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideCoinGeckoApi(
        okHttpClient: OkHttpClient,
        json: Json
    ): CoinGeckoApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(CoinGeckoApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(CoinGeckoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDevToApi(
        okHttpClient: OkHttpClient,
        json: Json
    ): DevToApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(DevToApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(DevToApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        val apiKey = BuildConfig.GEMINI_API_KEY
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}
