package com.jprojects.moodmate.presentation.screens.onboarding.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jprojects.moodmate.data.repository.authRepo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState


    var isUserSignedIn by mutableStateOf(false)
        private set


    init {
        checkAuthentication()
    }


    fun signUp(
        email: String,
        password: String,
        displayName: String,
    ) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        val result = authRepository.userSignUp(
            email = email, password = password,
            displayName = displayName
        )
        _authState.value = if (result.isSuccess) AuthState.Authenticated else AuthState.Error(
            result.exceptionOrNull()?.message ?: "Unknown error"
        )
    }


    fun signIn(email: String, password: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        try {
            val result = authRepository.userSignIn(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.Authenticated
            } else {
                throw result.exceptionOrNull() ?: Exception("Unknown error")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Sign-in failed")
        }
    }

    fun signOut() = viewModelScope.launch {
        _authState.value = AuthState.Loading
        val result = authRepository.userSignOut()
        _authState.value = if (result.isSuccess) AuthState.UnAuthenticated else AuthState.Error(
            result.exceptionOrNull()?.message ?: "Unknown error"
        )
    }

    private fun checkAuthentication() = viewModelScope.launch {
        isUserSignedIn = authRepository.isAuthenticated()
        _authState.value =
            if (isUserSignedIn) AuthState.Authenticated else AuthState.UnAuthenticated
    }


    fun sendPasswordResetEmailForgetPass(userEmail: String) {
        viewModelScope.launch {
            try {
                val result = authRepository.userForgotPassword(userEmail)
                if (result.isSuccess) {
                    _authState.value = AuthState.Authenticated
                } else {
                    throw result.exceptionOrNull() ?: Exception("Unknown error")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Message send failed")
            }
        }
    }

}