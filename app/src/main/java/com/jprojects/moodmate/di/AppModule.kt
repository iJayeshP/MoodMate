package com.jprojects.moodmate.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import com.jprojects.moodmate.data.repository.authRepo.AuthRepository
import com.jprojects.moodmate.data.repository.moodRepo.MoodRepository
import com.jprojects.moodmate.domain.repositoryimpl.authRepoImpl.AuthRepositoryImpl
import com.jprojects.moodmate.domain.repositoryimpl.moodRepoImpl.MoodRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(
                PersistentCacheSettings.newBuilder()
                    .build()
            )
            .build()

        firestore.firestoreSettings = settings
        return firestore
    }

    @Provides
    @Singleton
    fun providesFirebaseDatabase(): FirebaseDatabase {
        val database = FirebaseDatabase.getInstance()
        database.setPersistenceEnabled(true)
        return database
    }

    @Provides
    @Singleton
    fun providesAuthRepository(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase,
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firebaseDatabase)
    }

    @Provides
    @Singleton
    fun providesMoodRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        firebaseDatabase: FirebaseDatabase
    ): MoodRepository {
        return MoodRepositoryImpl(firebaseAuth, firebaseFirestore, firebaseDatabase)
    }
}
