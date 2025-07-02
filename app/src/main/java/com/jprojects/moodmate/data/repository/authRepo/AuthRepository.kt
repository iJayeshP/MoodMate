package com.jprojects.moodmate.data.repository.authRepo


interface AuthRepository {

    suspend fun userSignUp(
        email: String,
        password: String,
        displayName: String,
    ): Result<Unit>

    suspend fun userSignIn(email: String, password: String): Result<Unit>

    suspend fun isAuthenticated(): Boolean

    suspend fun userForgotPassword(email: String): Result<Unit>

    fun userSignOut(): Result<Unit>

}