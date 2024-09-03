package com.sarthkh.zestir.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sarthkh.zestir.ui.screens.GetStartedScreen
import com.sarthkh.zestir.ui.screens.LoginSignupScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "get_started") {
        composable("get_started") {
            GetStartedScreen(onGetStartedClick = {
                navController.navigate("login_signup")
            })
        }
        composable("login_signup") {
            LoginSignupScreen()
        }
    }
}