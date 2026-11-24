package com.techhub.app.features.interview.domain.usecase

import com.techhub.app.features.interview.domain.model.InterviewSession
import com.techhub.app.features.interview.domain.repository.InterviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInterviewHistoryUseCase @Inject constructor(
    private val repository: InterviewRepository
) {
    operator fun invoke(): Flow<List<InterviewSession>> {
        return repository.getSavedSessions()
    }
}
