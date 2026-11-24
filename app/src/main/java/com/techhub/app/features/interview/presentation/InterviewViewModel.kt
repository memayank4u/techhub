package com.techhub.app.features.interview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techhub.app.core.common.Resource
import com.techhub.app.features.interview.domain.model.InterviewSession
import com.techhub.app.features.interview.domain.usecase.EvaluateAnswerUseCase
import com.techhub.app.features.interview.domain.usecase.GenerateQuestionUseCase
import com.techhub.app.features.interview.domain.usecase.GetInterviewHistoryUseCase
import com.techhub.app.features.interview.domain.usecase.SaveInterviewSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewViewModel @Inject constructor(
    private val generateQuestionUseCase: GenerateQuestionUseCase,
    private val evaluateAnswerUseCase: EvaluateAnswerUseCase,
    private val getHistoryUseCase: GetInterviewHistoryUseCase,
    private val saveSessionUseCase: SaveInterviewSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterviewUiState())
    val uiState: StateFlow<InterviewUiState> = _uiState.asStateFlow()

    init {
        observeHistory()
    }

    fun onEvent(event: InterviewUiEvent) {
        when (event) {
            is InterviewUiEvent.SelectTopic -> _uiState.update { it.copy(selectedTopic = event.topic) }
            is InterviewUiEvent.SelectLevel -> _uiState.update { it.copy(selectedLevel = event.level) }
            is InterviewUiEvent.RequestQuestion -> generateQuestion()
            is InterviewUiEvent.UpdateAnswer -> _uiState.update { it.copy(userAnswer = event.answer) }
            is InterviewUiEvent.SubmitAnswer -> submitAnswer()
            is InterviewUiEvent.ResetSession -> {
                _uiState.update {
                    it.copy(
                        currentQuestion = "",
                        userAnswer = "",
                        streamingEvaluation = "",
                        step = InterviewStep.SETUP
                    )
                }
            }
            is InterviewUiEvent.ViewHistory -> _uiState.update { it.copy(step = InterviewStep.HISTORY) }
            is InterviewUiEvent.BackToSetup -> _uiState.update { it.copy(step = InterviewStep.SETUP) }
        }
    }

    private fun generateQuestion() {
        val topic = _uiState.value.selectedTopic
        val level = _uiState.value.selectedLevel

        viewModelScope.launch {
            generateQuestionUseCase(topic, level).collect { res ->
                when (res) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isGeneratingQuestion = true,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isGeneratingQuestion = false,
                                currentQuestion = res.data,
                                userAnswer = "",
                                streamingEvaluation = "",
                                step = InterviewStep.QUESTION_ACTIVE,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isGeneratingQuestion = false,
                                errorMessage = res.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun submitAnswer() {
        val question = _uiState.value.currentQuestion
        val answer = _uiState.value.userAnswer

        if (answer.isBlank()) return

        _uiState.update { it.copy(step = InterviewStep.EVALUATING, isEvaluatingAnswer = true) }

        viewModelScope.launch {
            evaluateAnswerUseCase(question, answer).collect { res ->
                when (res) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isEvaluatingAnswer = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isEvaluatingAnswer = false,
                                streamingEvaluation = res.data,
                                step = InterviewStep.EVALUATION_RESULT
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isEvaluatingAnswer = false,
                                errorMessage = res.message
                            )
                        }
                    }
                }
            }

            // Save to Room DB after evaluation completes
            val current = _uiState.value
            if (current.streamingEvaluation.isNotBlank()) {
                val score = extractScore(current.streamingEvaluation)
                saveSessionUseCase(
                    InterviewSession(
                        topic = current.selectedTopic,
                        level = current.selectedLevel,
                        question = question,
                        userAnswer = answer,
                        score = score,
                        evaluationFeedback = current.streamingEvaluation
                    )
                )
            }
        }
    }

    private fun extractScore(feedback: String): Int {
        val regex = Regex("(\\d+)/10")
        val match = regex.find(feedback)
        return match?.groupValues?.get(1)?.toIntOrNull() ?: 8
    }

    private fun observeHistory() {
        getHistoryUseCase()
            .onEach { history ->
                _uiState.update { it.copy(historySessions = history) }
            }
            .launchIn(viewModelScope)
    }
}
