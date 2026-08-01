package com.safesearch.ai.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme

enum class AppTheme {
    DEFAULT, FOREST, OCEAN, SUNSET, ASH
}

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_THEME = "selected_theme"
    
    private val _currentTheme = mutableStateOf(AppTheme.DEFAULT)
    val currentTheme: State<AppTheme> = _currentTheme

    fun initialize(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedTheme = prefs.getString(KEY_THEME, AppTheme.DEFAULT.name)
        _currentTheme.value = AppTheme.valueOf(savedTheme ?: AppTheme.DEFAULT.name)
    }

    fun setTheme(context: Context, theme: AppTheme) {
        _currentTheme.value = theme
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_THEME, theme.name).apply()
    }

    fun getColorScheme(theme: AppTheme): ColorScheme {
        return when (theme) {
            AppTheme.DEFAULT -> darkColorScheme(
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
            AppTheme.FOREST -> darkColorScheme(
                primary = Forest_Lightest,
                onPrimary = Forest_Darkest,
                secondary = Forest_Light,
                background = Forest_Darkest,
                surface = Forest_Dark,
                onBackground = Forest_Lightest,
                onSurface = Forest_Lightest,
                surfaceVariant = Forest_Medium,
                onSurfaceVariant = Forest_Lightest,
                outline = Forest_Medium,
                outlineVariant = Forest_Darkest
            )
            AppTheme.OCEAN -> darkColorScheme(
                primary = Ocean_Lightest,
                onPrimary = Ocean_Darkest,
                secondary = Ocean_Light,
                background = Ocean_Darkest,
                surface = Ocean_Dark,
                onBackground = Ocean_Lightest,
                onSurface = Ocean_Lightest,
                surfaceVariant = Ocean_Medium,
                onSurfaceVariant = Ocean_Lightest,
                outline = Ocean_Medium,
                outlineVariant = Ocean_Darkest
            )
            AppTheme.SUNSET -> darkColorScheme(
                primary = Sunset_Lightest,
                onPrimary = Sunset_Darkest,
                secondary = Sunset_Light,
                background = Sunset_Darkest,
                surface = Sunset_Dark,
                onBackground = Sunset_Lightest,
                onSurface = Sunset_Lightest,
                surfaceVariant = Sunset_Medium,
                onSurfaceVariant = Sunset_Lightest,
                outline = Sunset_Medium,
                outlineVariant = Sunset_Darkest
            )
            AppTheme.ASH -> darkColorScheme(
                primary = Ash_Lightest,
                onPrimary = Ash_Darkest,
                secondary = Ash_Light,
                background = Ash_Darkest,
                surface = Ash_Dark,
                onBackground = Ash_Lightest,
                onSurface = Ash_Lightest,
                surfaceVariant = Ash_Medium,
                onSurfaceVariant = Ash_Lightest,
                outline = Ash_Medium,
                outlineVariant = Ash_Darkest
            )
        }
    }
}
