package com.techhub.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.techhub.app.core.database.dao.ArticleDao
import com.techhub.app.core.database.dao.CoinDao
import com.techhub.app.core.database.dao.InterviewDao
import com.techhub.app.core.database.entity.ArticleEntity
import com.techhub.app.core.database.entity.CoinEntity
import com.techhub.app.core.database.entity.InterviewSessionEntity

@Database(
    entities = [
        CoinEntity::class,
        ArticleEntity::class,
        InterviewSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TechHubDatabase : RoomDatabase() {
    abstract val coinDao: CoinDao
    abstract val articleDao: ArticleDao
    abstract val interviewDao: InterviewDao
}
