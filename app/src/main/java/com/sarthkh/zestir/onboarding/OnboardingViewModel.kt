package com.sarthkh.zestir.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarthkh.zestir.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: OnboardingRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _onboardingState = MutableStateFlow<OnboardingState>(OnboardingState.Loading)
    val onboardingState: StateFlow<OnboardingState> = _onboardingState

    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep

    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    _userName.value = user.displayName ?: "there"
                    checkOnboardingStatus()
                } else {
                    _onboardingState.value = OnboardingState.Unauthenticated
                }
            }
        }
    }

    private suspend fun checkOnboardingStatus() {
        repository.isOnboardingComplete().collect { isCompleted ->
            if (isCompleted) {
                _onboardingState.value = OnboardingState.Completed
            } else {
                repository.getCurrentStep().collect { step ->
                    _currentStep.value = step
                    _onboardingState.value = OnboardingState.InProgress(step)
                }
            }
        }
    }

    fun saveStep(step: Int, data: Map<String, Any>) {
        viewModelScope.launch {
            repository.saveCurrentStep(step)
            repository.saveOnboardingData(data)
            goToNextStep()
        }
    }

    fun goToNextStep() {
        viewModelScope.launch {
            val nextStep = _currentStep.value + 1
            if (nextStep > TOTAL_STEPS) {
                completeOnboarding()
            } else {
                _currentStep.value = nextStep
                repository.saveCurrentStep(nextStep)
                _onboardingState.value = OnboardingState.InProgress(nextStep)
            }
        }
    }

    fun goToPreviousStep() {
        viewModelScope.launch {
            val previousStep = _currentStep.value - 1
            if (previousStep >= 1) {
                _currentStep.value = previousStep
                repository.saveCurrentStep(previousStep)
                _onboardingState.value = OnboardingState.InProgress(previousStep)
            }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            repository.completeOnboarding()
            _onboardingState.value = OnboardingState.Completed
        }
    }

    fun resetOnboarding() {
        viewModelScope.launch {
            repository.resetOnboarding()
            _currentStep.value = 1
            _onboardingState.value = OnboardingState.InProgress(1)
        }
    }

    companion object {
        const val TOTAL_STEPS = 12
    }
}

sealed class OnboardingState {
    object Loading : OnboardingState()
    object Unauthenticated : OnboardingState()
    data class InProgress(val currentStep: Int) : OnboardingState()
    object Completed : OnboardingState()
}