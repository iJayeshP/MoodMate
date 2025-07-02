package com.jprojects.moodmate.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val displayName: String = "",
    val userEmail: String = "",

    val userUUID: String = "",

    )

