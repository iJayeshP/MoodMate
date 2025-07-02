package com.jprojects.moodmate.domain.repositoryimpl.authRepoImpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jprojects.moodmate.data.repository.authRepo.AuthRepository
import com.jprojects.moodmate.domain.model.UserDetails
import com.jprojects.moodmate.util.Constants
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
) : AuthRepository {
    override suspend fun userSignUp(
        email: String,
        password: String,
        displayName: String
    ): Result<Unit> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            authResult.user?.let { user ->
                val userRef = firebaseDatabase.reference.child(Constants.USER_COLLECTION).child(user.uid)

                val userDetails = UserDetails(
                    userUUID = user.uid,
                    userEmail = email,
                    displayName = displayName,
                )

                userRef.setValue(userDetails).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun userSignIn(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun userForgotPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)

        }
    }

    override fun userSignOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)

        }
    }
}