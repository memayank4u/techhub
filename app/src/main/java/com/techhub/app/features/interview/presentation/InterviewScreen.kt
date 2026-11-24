package com.techhub.app.features.interview.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techhub.app.R
import com.techhub.app.core.designsystem.components.EmptyStateView
import com.techhub.app.core.designsystem.components.TechHubTopBar
import com.techhub.app.core.designsystem.theme.MarketGreen
import com.techhub.app.core.designsystem.theme.MarketGreenContainer
import com.techhub.app.core.designsystem.theme.TechHubTheme
import com.techhub.app.features.interview.domain.model.InterviewSession

@Composable
fun InterviewScreen(
    viewModel: InterviewViewModel = hiltViewModel(),
    onOpenThemeSettings: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TechHubTopBar(
                title = stringResource(id = R.string.interview_title),
                navigationIcon = {
                    if (state.step != InterviewStep.SETUP) {
                        IconButton(onClick = { viewModel.onEvent(InterviewUiEvent.BackToSetup) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.common_close)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onOpenThemeSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = stringResource(id = R.string.theme_settings_tooltip),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        if (state.step == InterviewStep.HISTORY) {
                            viewModel.onEvent(InterviewUiEvent.BackToSetup)
                        } else {
                            viewModel.onEvent(InterviewUiEvent.ViewHistory)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = stringResource(id = R.string.interview_history_title)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.step) {
                InterviewStep.SETUP -> SetupView(state = state, viewModel = viewModel)
                InterviewStep.QUESTION_ACTIVE -> QuestionView(state = state, viewModel = viewModel)
                InterviewStep.EVALUATING, InterviewStep.EVALUATION_RESULT -> EvaluationView(state = state, viewModel = viewModel)
                InterviewStep.HISTORY -> HistoryView(state = state)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SetupView(
    state: InterviewUiState,
    viewModel: InterviewViewModel
) {
    val topics = listOf(
        stringResource(id = R.string.interview_topic_android),
        stringResource(id = R.string.interview_topic_kotlin),
        stringResource(id = R.string.interview_topic_architecture),
        stringResource(id = R.string.interview_topic_dsa)
    )

    val levels = listOf(
        stringResource(id = R.string.interview_level_junior),
        stringResource(id = R.string.interview_level_mid),
        stringResource(id = R.string.interview_level_senior)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(TechHubTheme.dimens.spacingMedium)
    ) {
        // Hero AI Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(TechHubTheme.dimens.radiusLarge),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
                    .padding(TechHubTheme.dimens.spacingLarge)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(id = R.string.hero_ai_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingXXSmall))
                        Text(
                            text = stringResource(id = R.string.hero_ai_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(TechHubTheme.dimens.iconLarge)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingLarge))

        Text(
            text = stringResource(id = R.string.interview_select_topic),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingSmall))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingSmall),
            verticalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingSmall)
        ) {
            topics.forEach { topic ->
                FilterChip(
                    selected = state.selectedTopic == topic,
                    onClick = { viewModel.onEvent(InterviewUiEvent.SelectTopic(topic)) },
                    label = { Text(text = topic) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingLarge))

        Text(
            text = stringResource(id = R.string.interview_select_level),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingSmall))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingSmall),
            verticalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingSmall)
        ) {
            levels.forEach { level ->
                FilterChip(
                    selected = state.selectedLevel == level,
                    onClick = { viewModel.onEvent(InterviewUiEvent.SelectLevel(level)) },
                    label = { Text(text = level) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingXLarge))

        Button(
            onClick = { viewModel.onEvent(InterviewUiEvent.RequestQuestion) },
            enabled = !state.isGeneratingQuestion,
            shape = RoundedCornerShape(TechHubTheme.dimens.radiusMedium),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            if (state.isGeneratingQuestion) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(TechHubTheme.dimens.iconMedium)
                )
                Spacer(modifier = Modifier.width(TechHubTheme.dimens.spacingSmall))
                Text(
                    text = stringResource(id = R.string.interview_start_button),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuestionView(
    state: InterviewUiState,
    viewModel: InterviewViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(TechHubTheme.dimens.spacingMedium)
    ) {
        // Question Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(TechHubTheme.dimens.radiusLarge),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(TechHubTheme.dimens.spacingLarge)) {
                Surface(
                    shape = RoundedCornerShape(TechHubTheme.dimens.radiusSmall),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = stringResource(id = R.string.interview_question_label),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingMedium))
                Text(
                    text = state.currentQuestion,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingMedium))

        // Candidate Answer Field
        OutlinedTextField(
            value = state.userAnswer,
            onValueChange = { viewModel.onEvent(InterviewUiEvent.UpdateAnswer(it)) },
            placeholder = { Text(text = stringResource(id = R.string.interview_answer_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(TechHubTheme.dimens.radiusMedium),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingMedium))

        Button(
            onClick = { viewModel.onEvent(InterviewUiEvent.SubmitAnswer) },
            enabled = state.userAnswer.isNotBlank(),
            shape = RoundedCornerShape(TechHubTheme.dimens.radiusMedium),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = stringResource(id = R.string.interview_submit_answer),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EvaluationView(
    state: InterviewUiState,
    viewModel: InterviewViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(TechHubTheme.dimens.spacingMedium)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(TechHubTheme.dimens.radiusLarge),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(TechHubTheme.dimens.spacingLarge)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.interview_feedback_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (state.isEvaluatingAnswer) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp)
                    }
                }

                Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingMedium))

                if (state.isEvaluatingAnswer && state.streamingEvaluation.isBlank()) {
                    Text(
                        text = stringResource(id = R.string.interview_evaluating),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = state.streamingEvaluation,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingLarge))

        if (!state.isEvaluatingAnswer) {
            Button(
                onClick = { viewModel.onEvent(InterviewUiEvent.ResetSession) },
                shape = RoundedCornerShape(TechHubTheme.dimens.radiusMedium),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.interview_new_question),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HistoryView(
    state: InterviewUiState
) {
    if (state.historySessions.isEmpty()) {
        EmptyStateView(
            title = stringResource(id = R.string.interview_history_empty),
            subtitle = stringResource(id = R.string.articles_empty_bookmarks_sub),
            actionText = null
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(TechHubTheme.dimens.spacingMedium),
            verticalArrangement = Arrangement.spacedBy(TechHubTheme.dimens.spacingMedium)
        ) {
            items(state.historySessions, key = { it.id }) { session ->
                HistoryItem(session = session)
            }
        }
    }
}

@Composable
private fun HistoryItem(
    session: InterviewSession,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(TechHubTheme.dimens.radiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(TechHubTheme.dimens.spacingMedium)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(TechHubTheme.dimens.radiusSmall),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "${session.topic} • ${session.level}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(TechHubTheme.dimens.radiusSmall),
                    color = MarketGreenContainer
                ) {
                    Text(
                        text = stringResource(id = R.string.interview_score_label, session.score),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MarketGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingMedium))

            Text(
                text = session.question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(TechHubTheme.dimens.spacingSmall))

            Text(
                text = session.evaluationFeedback,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
