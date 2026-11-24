package com.techhub.app.features.interview.domain.repository

import com.techhub.app.core.common.Resource
import com.techhub.app.features.interview.domain.model.InterviewSession
import kotlinx.coroutines.flow.Flow

interface InterviewRepository {
    fun generateQuestion(topic: String, level: String): Flow<Resource<String>>
    fun evaluateAnswer(question: String, answer: String): Flow<Resource<String>>
    suspend fun saveSession(session: InterviewSession): Long
    fun getSavedSessions(): Flow<List<InterviewSession>>
    suspend fun deleteSession(id: Long)
}
