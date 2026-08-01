package com.safesearch.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.safesearch.ai.ui.theme.SafeSearchAITheme
import com.safesearch.ai.ui.LoginScreen
import com.safesearch.ai.ui.ChatScreen
import com.safesearch.ai.ui.AdminDashboard
import com.safesearch.ai.ui.ChooseThemeScreen
import com.safesearch.ai.ui.theme.ThemeManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.initialize(this)
        setContent {
            SafeSearchAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("chat") { ChatScreen(navController) }
        composable("admin") { AdminDashboard(navController) }
        composable("choose_theme") { ChooseThemeScreen(navController) }
    }
}
