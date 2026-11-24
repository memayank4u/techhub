package com.techhub.app.features.interview.domain.model

data class InterviewSession(
    val id: Long = 0,
    val topic: String,
    val level: String,
    val question: String,
    val userAnswer: String,
    val score: Int,
    val evaluationFeedback: String,
    val timestamp: Long = System.currentTimeMillis()
)
