package com.safesearch.ai.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AI_White,
    onPrimary = AI_Black,
    secondary = AI_LightGrey,
    background = AI_Black,
    surface = AI_DarkGrey,
    onBackground = AI_White,
    onSurface = AI_White,
    surfaceVariant = AI_MediumGrey,
    onSurfaceVariant = AI_LightGrey,
    outline = AI_MediumGrey,
    outlineVariant = AI_DarkGrey
)

@Composable
fun SafeSearchAITheme(
    content: @Composable () -> Unit
) {
    val currentTheme by ThemeManager.currentTheme
    val colorScheme = ThemeManager.getColorScheme(currentTheme)
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false 
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
