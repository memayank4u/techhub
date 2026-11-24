package com.techhub.app.features.interview.presentation

sealed interface InterviewUiEvent {
    data class SelectTopic(val topic: String) : InterviewUiEvent
    data class SelectLevel(val level: String) : InterviewUiEvent
    data object RequestQuestion : InterviewUiEvent
    data class UpdateAnswer(val answer: String) : InterviewUiEvent
    data object SubmitAnswer : InterviewUiEvent
    data object ResetSession : InterviewUiEvent
    data object ViewHistory : InterviewUiEvent
    data object BackToSetup : InterviewUiEvent
}
