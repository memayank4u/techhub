package com.techhub.app.features.interview.domain.usecase

import com.techhub.app.features.interview.domain.model.InterviewSession
import com.techhub.app.features.interview.domain.repository.InterviewRepository
import javax.inject.Inject

class SaveInterviewSessionUseCase @Inject constructor(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke(session: InterviewSession): Long {
        return repository.saveSession(session)
    }
}
