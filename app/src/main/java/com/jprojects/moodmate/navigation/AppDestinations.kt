package com.jprojects.moodmate.navigation

import kotlinx.serialization.Serializable

sealed class AppDestinations {

    @Serializable
    data object GetStartedScreen : AppDestinations()

    @Serializable
    data object SignInScreen : AppDestinations()

    @Serializable
    data object SignUpScreen : AppDestinations()

    @Serializable
    data object HomeScreen : AppDestinations()

    @Serializable
    data object AddMoodScreen : AppDestinations()

}