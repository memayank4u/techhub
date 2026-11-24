package com.techhub.app.features.articles.di

import com.techhub.app.features.articles.data.repository.ArticleRepositoryImpl
import com.techhub.app.features.articles.domain.repository.ArticleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ArticleModule {

    @Binds
    @Singleton
    abstract fun bindArticleRepository(
        impl: ArticleRepositoryImpl
    ): ArticleRepository
}
