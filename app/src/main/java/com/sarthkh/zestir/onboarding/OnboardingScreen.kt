package com.sarthkh.zestir.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarthkh.zestir.R

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val onboardingState by viewModel.onboardingState.collectAsState()
    val currentStep by viewModel.currentStep.collectAsState()
    val pagerState = rememberPagerState(pageCount = { OnboardingViewModel.TOTAL_STEPS })

    LaunchedEffect(onboardingState) {
        if (onboardingState is OnboardingState.Completed) {
            onComplete()
        }
    }

    LaunchedEffect(currentStep) {
        pagerState.animateScrollToPage(currentStep - 1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { currentStep.toFloat() / OnboardingViewModel.TOTAL_STEPS },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page + 1) {
                    1 -> WelcomeStep(viewModel)
                    2 -> PrimaryGoalSelectionStep(
                        onGoalSelected = { goal ->
                            viewModel.saveStep(2, mapOf("primaryGoal" to goal))
                        }
                    )
                    // Add other steps here
                    else -> Text("Step ${page + 1}")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {
                        if (currentStep > 1) {
                            viewModel.goToPreviousStep()
                        }
                    },
                    enabled = currentStep > 1
                ) {
                    Text("Previous")
                }
                Button(
                    onClick = {
                        if (currentStep < OnboardingViewModel.TOTAL_STEPS) {
                            viewModel.goToNextStep()
                        } else {
                            viewModel.completeOnboarding()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(if (currentStep == OnboardingViewModel.TOTAL_STEPS) "Finish" else "Next")
                }
            }
        }
    }
}

@Composable
fun WelcomeStep(viewModel: OnboardingViewModel) {
    val userName by viewModel.userName.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_light),
            contentDescription = "Zestir Logo",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Welcome to Zestir, $userName!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Zestir uniquely integrates fitness and productivity with AI-driven personalization and real-time form correction.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Let's get started with your personalized journey!",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun PrimaryGoalSelectionStep(onGoalSelected: (String) -> Unit) {
    var selectedGoal by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "What's your primary goal?",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        GoalOption(
            title = "Fitness Focus",
            description = "Weight management, strength improvement, flexibility",
            icon = Icons.Default.FitnessCenter,
            isSelected = selectedGoal == "fitness",
            onSelect = {
                selectedGoal = "fitness"
                onGoalSelected("fitness")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        GoalOption(
            title = "Productivity Focus",
            description = "Time management, focus enhancement, task organization",
            icon = Icons.Default.Task,
            isSelected = selectedGoal == "productivity",
            onSelect = {
                selectedGoal = "productivity"
                onGoalSelected("productivity")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        GoalOption(
            title = "Holistic Wellness",
            description = "Stress reduction, better sleep, energy optimization",
            icon = Icons.Default.SelfImprovement,
            isSelected = selectedGoal == "wellness",
            onSelect = {
                selectedGoal = "wellness"
                onGoalSelected("wellness")
            }
        )
    }
}

@Composable
fun GoalOption(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}