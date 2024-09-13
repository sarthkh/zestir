package com.sarthkh.zestir.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sarthkh.zestir.auth.AuthState
import com.sarthkh.zestir.auth.AuthViewModel
import com.sarthkh.zestir.ui.components.NetworkStatusIndicator
import com.sarthkh.zestir.ui.screens.GetStartedScreen
import com.sarthkh.zestir.ui.screens.HomeScreen
import com.sarthkh.zestir.ui.screens.LoginSignupScreen
import com.sarthkh.zestir.utils.NetworkConnectivity

@Composable
fun NavGraph(
    authViewModel: AuthViewModel = hiltViewModel(),
    networkConnectivity: NetworkConnectivity
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val isOnline by networkConnectivity.isNetworkAvailable.collectAsState(initial = true)

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> navController.navigate("home") {
                popUpTo("get_started") { inclusive = true }
            }

            is AuthState.Unauthenticated -> navController.navigate("get_started") {
                popUpTo("home") { inclusive = true }
            }

            else -> {} // Do nothing for other states
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = if (authViewModel.isUserAuthenticated()) "home" else "get_started"
        ) {
            composable("get_started") {
                GetStartedScreen(onGetStartedClick = {
                    navController.navigate("login_signup")
                })
            }
            composable("login_signup") {
                LoginSignupScreen(
                    onAuthSuccess = {
                        navController.navigate("home") {
                            popUpTo("get_started") { inclusive = true }
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
}