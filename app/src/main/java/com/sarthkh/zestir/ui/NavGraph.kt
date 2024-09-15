package com.sarthkh.zestir.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sarthkh.zestir.auth.AuthState
import com.sarthkh.zestir.auth.AuthViewModel
import com.sarthkh.zestir.onboarding.OnboardingScreen
import com.sarthkh.zestir.onboarding.OnboardingState
import com.sarthkh.zestir.onboarding.OnboardingViewModel
import com.sarthkh.zestir.ui.components.NetworkStatusIndicator
import com.sarthkh.zestir.ui.screens.GetStartedScreen
import com.sarthkh.zestir.ui.screens.HomeScreen
import com.sarthkh.zestir.ui.screens.LoginSignupScreen
import com.sarthkh.zestir.utils.NetworkConnectivity

@Composable
fun NavGraph(
    authViewModel: AuthViewModel,
    onboardingViewModel: OnboardingViewModel,
    networkConnectivity: NetworkConnectivity
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val onboardingState by onboardingViewModel.onboardingState.collectAsState()
    val isOnline by networkConnectivity.isNetworkAvailable.collectAsState(initial = true)

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            LaunchedEffect(authState, onboardingState) {
                when {
                    authState is AuthState.Unauthenticated -> navController.navigate("get_started") {
                        popUpTo("splash") { inclusive = true }
                    }

                    authState is AuthState.Authenticated && onboardingState is OnboardingState.Completed ->
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }

                    authState is AuthState.Authenticated && onboardingState is OnboardingState.InProgress ->
                        navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }
                }
            }
        }

        composable("get_started") {
            GetStartedScreen(onGetStartedClick = {
                navController.navigate("login_signup")
            })
        }

        composable("login_signup") {
            LoginSignupScreen(
                onAuthSuccess = {
                    navController.navigate("onboarding") {
                        popUpTo("login_signup") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("get_started") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }

    NetworkStatusIndicator(isOnline = isOnline)
}