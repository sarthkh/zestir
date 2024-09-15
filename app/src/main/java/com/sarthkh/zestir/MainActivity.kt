package com.sarthkh.zestir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sarthkh.zestir.auth.AuthState
import com.sarthkh.zestir.auth.AuthViewModel
import com.sarthkh.zestir.onboarding.OnboardingState
import com.sarthkh.zestir.onboarding.OnboardingViewModel
import com.sarthkh.zestir.ui.NavGraph
import com.sarthkh.zestir.ui.theme.ZestirTheme
import com.sarthkh.zestir.utils.NetworkConnectivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val onboardingViewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val networkConnectivity = NetworkConnectivity(applicationContext)

        setContent {
            val authState by authViewModel.authState.collectAsState()
            val onboardingState by onboardingViewModel.onboardingState.collectAsState()

            splashScreen.setKeepOnScreenCondition {
                authState is AuthState.Initial || onboardingState is OnboardingState.Loading
            }

            ZestirTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        authViewModel = authViewModel,
                        onboardingViewModel = onboardingViewModel,
                        networkConnectivity = networkConnectivity
                    )
                }
            }
        }
    }
}