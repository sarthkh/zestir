package com.sarthkh.zestir.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun NetworkStatusIndicator(isOnline: Boolean) {
    var visible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var isInitialState by remember { mutableStateOf(true) }

    LaunchedEffect(isOnline) {
        if (isInitialState) {
            isInitialState = false
            if (!isOnline) {
                visible = true
                message = "You're offline"
            }
        } else {
            if (!isOnline) {
                visible = true
                message = "You're offline"
            } else {
                message = "Back online"
                visible = true
                delay(3000)
                visible = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        if (!isOnline) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primaryContainer
                    )
                    .animateContentSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isOnline) Icons.Filled.Wifi else Icons.Filled.WifiOff,
                        contentDescription = "Network status",
                        tint = if (!isOnline) MaterialTheme.colorScheme.onError
                        else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = message,
                        color = if (!isOnline) MaterialTheme.colorScheme.onError
                        else MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}