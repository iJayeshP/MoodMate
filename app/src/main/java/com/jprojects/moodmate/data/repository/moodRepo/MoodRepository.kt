package com.jprojects.moodmate.data.repository.moodRepo

import com.jprojects.moodmate.domain.model.MoodDetails
import com.jprojects.moodmate.domain.model.UserDetails
import kotlinx.coroutines.flow.Flow

interface MoodRepository {

    suspend fun addMood(mood: String, moodReason: String): Result<Unit>

    suspend fun fetchMood(): Flow<List<MoodDetails>>

    suspend fun fetchUserDetails(): Flow<UserDetails>

}