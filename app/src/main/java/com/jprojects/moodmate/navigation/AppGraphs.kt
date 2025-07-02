package com.jprojects.moodmate.navigation

import kotlinx.serialization.Serializable

sealed class AppGraphs {

    @Serializable
    data object Auth: AppGraphs()

    @Serializable
    data object Home: AppGraphs()

}