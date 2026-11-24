package com.techhub.app.features.interview.presentation

import com.techhub.app.features.interview.domain.model.InterviewSession

enum class InterviewStep {
    SETUP,
    QUESTION_ACTIVE,
    EVALUATING,
    EVALUATION_RESULT,
    HISTORY
}

data class InterviewUiState(
    val selectedTopic: String = "Android & Jetpack",
    val selectedLevel: String = "Senior / Lead (5+ yrs)",
    val currentQuestion: String = "",
    val userAnswer: String = "",
    val isGeneratingQuestion: Boolean = false,
    val isEvaluatingAnswer: Boolean = false,
    val streamingEvaluation: String = "",
    val step: InterviewStep = InterviewStep.SETUP,
    val historySessions: List<InterviewSession> = emptyList(),
    val errorMessage: String? = null
)
