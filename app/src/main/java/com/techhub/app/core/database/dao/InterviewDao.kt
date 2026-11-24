package com.techhub.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techhub.app.core.database.entity.InterviewSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterviewDao {

    @Query("SELECT * FROM interview_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<InterviewSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: InterviewSessionEntity): Long

    @Query("DELETE FROM interview_sessions WHERE id = :id")
    suspend fun deleteSession(id: Long)

    @Query("DELETE FROM interview_sessions")
    suspend fun clearAll()
}
