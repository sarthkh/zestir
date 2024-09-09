package com.sarthkh.zestir.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sarthkh.zestir.auth.AuthViewModel
import com.sarthkh.zestir.auth.AuthState
import com.sarthkh.zestir.ui.screens.GetStartedScreen
import com.sarthkh.zestir.ui.screens.LoginSignupScreen
import com.sarthkh.zestir.ui.screens.HomeScreen

@Composable
fun NavGraph(authViewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = when (authState) {
            is AuthState.Authenticated -> "home"
            else -> "get_started"
        }
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
}