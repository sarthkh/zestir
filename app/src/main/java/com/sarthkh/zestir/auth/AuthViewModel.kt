package com.sarthkh.zestir.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sarthkh.zestir.onboarding.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    init {
        viewModelScope.launch {
            repository.currentUser.collect { user ->
                _authState.value =
                    if (user != null) AuthState.Authenticated(user) else AuthState.Unauthenticated
            }
        }
    }

    fun signUp(email: String, password: String) = handleAuthAction {
        repository.signUp(email, password).also {
            onboardingRepository.resetOnboarding()
        }
    }

    fun login(email: String, password: String) = handleAuthAction {
        repository.login(email, password)
    }

    fun googleSignIn(idToken: String) = handleAuthAction {
        repository.googleSignIn(idToken).also {
            onboardingRepository.resetOnboarding()
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Unauthenticated
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.sendPasswordResetEmail(email)
                .onSuccess { _authState.value = AuthState.PasswordResetEmailSent }
                .onFailure {
                    _authState.value =
                        AuthState.Error(it.message ?: "Failed to send password reset email")
                }
        }
    }

    fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.updatePassword(newPassword)
                .onSuccess { _authState.value = AuthState.PasswordUpdated }
                .onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Failed to update password")
                }
        }
    }

    private fun handleAuthAction(action: suspend () -> Result<FirebaseUser>) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            action()
                .onSuccess { _authState.value = AuthState.Authenticated(it) }
                .onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Authentication failed")
                }
        }
    }

    fun isUserAuthenticated(): Boolean = repository.isUserAuthenticated()
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
    object PasswordResetEmailSent : AuthState()
    object PasswordUpdated : AuthState()
}