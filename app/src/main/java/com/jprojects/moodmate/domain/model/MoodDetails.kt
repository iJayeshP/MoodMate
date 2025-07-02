package com.jprojects.moodmate.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MoodDetails(
    val mood: String = "",
    val moodReason: String = "",
    val timestamp: Long = 0L,
    val userUUID: String = ""
)