package com.littlegig.app.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.littlegig.app.presentation.navigation.LittleGigNavigation
import com.littlegig.app.presentation.theme.LittleGigTheme

@Composable
fun LittleGigApp() {
    LittleGigTheme {
        val systemUiController = rememberSystemUiController()
        val isDark = isSystemInDarkTheme()
        
        // Set system bars to transparent for edge-to-edge design
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !isDark
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent
            )
        }
        
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            LittleGigNavigation()
        }
    }
}