package com.jprojects.moodmate

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.jprojects.moodmate.navigation.AppGraphs
import com.jprojects.moodmate.navigation.AppNavigation
import com.jprojects.moodmate.presentation.screens.onboarding.auth.AuthViewModel
import com.jprojects.moodmate.ui.theme.MoodMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.Companion.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.Companion.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        setContent {
            MoodMateTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navHostController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    val checkAuth = authViewModel.isUserSignedIn

    val startScreen = if (checkAuth) {
        AppGraphs.Auth
    } else {
        AppGraphs.Auth
    }
    AppNavigation(
        navController = navHostController, authViewModel = authViewModel, startScreen = startScreen
    )
}