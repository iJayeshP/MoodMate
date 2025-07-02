package com.jprojects.moodmate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jprojects.moodmate.presentation.screens.home.HomeScreen
import com.jprojects.moodmate.presentation.screens.home.addMood.AddMoodScreen
import com.jprojects.moodmate.presentation.screens.onboarding.GetStartedScreen
import com.jprojects.moodmate.presentation.screens.onboarding.auth.AuthViewModel
import com.jprojects.moodmate.presentation.screens.onboarding.auth.signup.SignUpScreen
import com.jprojects.moodmate.presentation.screens.onboarding.auth.singin.SignInScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    startScreen: AppGraphs
) {
    NavHost(navController = navController, startDestination = startScreen) {

        navigation<AppGraphs.Auth>(startDestination = AppDestinations.GetStartedScreen) {
            composable<AppDestinations.GetStartedScreen> {
                GetStartedScreen(
                    onSignUpClick = {
                        navController.navigate(AppDestinations.SignUpScreen)

                    },
                    onSignInClick = {
                        navController.navigate(AppDestinations.SignInScreen)

                    }
                )
            }

            composable<AppDestinations.SignInScreen> {
                SignInScreen(
                    authViewModel = authViewModel,
                    onSignUpScreen = {
                        navController.navigate(AppDestinations.SignUpScreen)
                    },
                    onAuthenticated = {
                        navController.navigate(AppDestinations.HomeScreen) {
                            popUpToRoute
                        }
                    }
                )
            }

            composable<AppDestinations.SignUpScreen> {
                SignUpScreen(authViewModel = authViewModel, onAuthenticated = {
                    navController.navigate(AppGraphs.Home) {
                        popUpToRoute
                    }
                })
            }
        }

        navigation<AppGraphs.Home>(startDestination = AppDestinations.HomeScreen) {

            composable<AppDestinations.HomeScreen> {
                HomeScreen(
                    onAddMoodClick = {
                        navController.navigate(AppDestinations.AddMoodScreen)
                    },
                    onSignOut = {
                        navController.navigate(AppGraphs.Auth)
                    }
                )
            }

            composable<AppDestinations.AddMoodScreen> {
                AddMoodScreen(
                    onBackPressed = {
                        navController.navigateUp()
                    },
                    onMoodSubmitted = {
                        navController.navigateUp()
                    }
                )
            }

        }
    }


}