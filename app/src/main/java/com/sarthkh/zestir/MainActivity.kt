package com.sarthkh.zestir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.sarthkh.zestir.auth.AuthViewModel
import com.sarthkh.zestir.ui.NavGraph
import com.sarthkh.zestir.ui.theme.ZestirTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()

        FirebaseApp.initializeApp(this)

        setContent {
            ZestirTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(authViewModel = authViewModel)
                }
            }
        }

        // Observe auth state changes
        lifecycleScope.launch {
            authViewModel.authState.collectLatest { state ->
                // Handle auth state changes (e.g., navigate to appropriate screen)
            }
        }
    }
}