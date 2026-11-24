package com.techhub.app.features.interview.presentation

import app.cash.turbine.test
import com.techhub.app.core.common.Resource
import com.techhub.app.features.interview.domain.model.InterviewSession
import com.techhub.app.features.interview.domain.usecase.EvaluateAnswerUseCase
import com.techhub.app.features.interview.domain.usecase.GenerateQuestionUseCase
import com.techhub.app.features.interview.domain.usecase.GetInterviewHistoryUseCase
import com.techhub.app.features.interview.domain.usecase.SaveInterviewSessionUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InterviewViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val generateQuestionUseCase: GenerateQuestionUseCase = mockk()
    private val evaluateAnswerUseCase: EvaluateAnswerUseCase = mockk()
    private val getHistoryUseCase: GetInterviewHistoryUseCase = mockk()
    private val saveSessionUseCase: SaveInterviewSessionUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getHistoryUseCase() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `RequestQuestion triggers generation and transitions step to QUESTION_ACTIVE`() = runTest {
        // Given
        val question = "Explain the difference between Flow and LiveData in Android."
        every { generateQuestionUseCase(any(), any()) } returns flowOf(
            Resource.Loading(),
            Resource.Success(question)
        )

        val viewModel = InterviewViewModel(
            generateQuestionUseCase,
            evaluateAnswerUseCase,
            getHistoryUseCase,
            saveSessionUseCase
        )

        // When
        viewModel.onEvent(InterviewUiEvent.RequestQuestion)

        // Then
        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val state = expectMostRecentItem()
            assertFalse(state.isGeneratingQuestion)
            assertEquals(InterviewStep.QUESTION_ACTIVE, state.step)
            assertEquals(question, state.currentQuestion)
        }
    }

    @Test
    fun `SubmitAnswer streams evaluation and updates step to EVALUATION_RESULT`() = runTest {
        // Given
        val question = "Explain Coroutines Dispatchers."
        val answer = "Dispatchers.IO is for disk/network, Dispatchers.Main is for UI."
        val feedback = "• Score: 9/10\n• Key Strengths: Accurate dispatcher usage."

        every { generateQuestionUseCase(any(), any()) } returns flowOf(Resource.Success(question))
        every { evaluateAnswerUseCase(question, answer) } returns flowOf(
            Resource.Loading(),
            Resource.Success(feedback)
        )
        coEvery { saveSessionUseCase(any()) } returns 1L

        val viewModel = InterviewViewModel(
            generateQuestionUseCase,
            evaluateAnswerUseCase,
            getHistoryUseCase,
            saveSessionUseCase
        )

        viewModel.onEvent(InterviewUiEvent.RequestQuestion)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(InterviewUiEvent.UpdateAnswer(answer))
        viewModel.onEvent(InterviewUiEvent.SubmitAnswer)

        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val state = expectMostRecentItem()
            assertFalse(state.isEvaluatingAnswer)
            assertEquals(InterviewStep.EVALUATION_RESULT, state.step)
            assertEquals(feedback, state.streamingEvaluation)
        }
    }
}
