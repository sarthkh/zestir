package com.sarthkh.zestir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarthkh.zestir.auth.AuthState
import com.sarthkh.zestir.auth.AuthViewModel
import com.sarthkh.zestir.ui.NavGraph
import com.sarthkh.zestir.ui.theme.ZestirTheme
import com.sarthkh.zestir.utils.NetworkConnectivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val networkConnectivity = NetworkConnectivity(applicationContext)

        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            splashScreen.setKeepOnScreenCondition { authViewModel.authState.value == AuthState.Initial }

            ZestirTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(authViewModel, networkConnectivity)
                }
            }
        }
    }
}