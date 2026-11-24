package com.techhub.app.core.database.di

import android.content.Context
import androidx.room.Room
import com.techhub.app.core.database.TechHubDatabase
import com.techhub.app.core.database.dao.ArticleDao
import com.techhub.app.core.database.dao.CoinDao
import com.techhub.app.core.database.dao.InterviewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTechHubDatabase(
        @ApplicationContext context: Context
    ): TechHubDatabase {
        return Room.databaseBuilder(
            context,
            TechHubDatabase::class.java,
            "techhub_database.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCoinDao(database: TechHubDatabase): CoinDao = database.coinDao

    @Provides
    fun provideArticleDao(database: TechHubDatabase): ArticleDao = database.articleDao

    @Provides
    fun provideInterviewDao(database: TechHubDatabase): InterviewDao = database.interviewDao
}
