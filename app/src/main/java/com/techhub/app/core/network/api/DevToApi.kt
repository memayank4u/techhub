package com.techhub.app.core.network.api

import com.techhub.app.features.articles.data.dto.ArticleDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DevToApi {

    @GET("articles")
    suspend fun getArticles(
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1,
        @Query("tag") tag: String? = null
    ): List<ArticleDto>

    companion object {
        const val BASE_URL = "https://dev.to/api/"
    }
}
