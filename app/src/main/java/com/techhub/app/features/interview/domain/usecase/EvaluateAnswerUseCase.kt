package com.techhub.app.features.interview.domain.usecase

import com.techhub.app.core.common.Resource
import com.techhub.app.features.interview.domain.repository.InterviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EvaluateAnswerUseCase @Inject constructor(
    private val repository: InterviewRepository
) {
    operator fun invoke(question: String, answer: String): Flow<Resource<String>> {
        return repository.evaluateAnswer(question, answer)
    }
}
