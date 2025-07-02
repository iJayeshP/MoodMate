package com.jprojects.moodmate.presentation.screens.onboarding.auth

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class UpdateState {
    object Idle : UpdateState()
    object Loading : UpdateState()
    object Success : UpdateState()
    object UnSuccessful : UpdateState()
    data class Error(val message: String) : UpdateState()
}