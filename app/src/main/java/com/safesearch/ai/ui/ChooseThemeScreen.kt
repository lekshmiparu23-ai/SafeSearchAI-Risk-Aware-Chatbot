package com.safesearch.ai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.safesearch.ai.ui.theme.AppTheme
import com.safesearch.ai.ui.theme.ThemeManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseThemeScreen(navController: NavController) {
    val context = LocalContext.current
    val currentTheme by ThemeManager.currentTheme
    
    val themes = listOf(
        ThemeItem("Default", AppTheme.DEFAULT, listOf(Color(0xFF000000), Color(0xFF161616), Color(0xFFFFFFFF))),
        ThemeItem("Verdant Forest", AppTheme.FOREST, listOf(Color(0xFF728156), Color(0xFF88976C), Color(0xFFE7F5DC))),
        ThemeItem("Deep Ocean", AppTheme.OCEAN, listOf(Color(0xFF021024), Color(0xFF052659), Color(0xFFC1E8FF))),
        ThemeItem("Sunset Glow", AppTheme.SUNSET, listOf(Color(0xFF4C1D3D), Color(0xFF852E4E), Color(0xFFFFBB94))),
        ThemeItem("Midnight Ash", AppTheme.ASH, listOf(Color(0xFF2A0800), Color(0xFF775144), Color(0xFFF4D8D8)))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Theme", color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Select a theme to personalize your experience.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(themes) { theme ->
                    ThemePreviewCard(
                        theme = theme,
                        isSelected = currentTheme == theme.id,
                        onClick = { ThemeManager.setTheme(context, theme.id) }
                    )
                }
            }
        }
    }
}

data class ThemeItem(
    val name: String,
    val id: AppTheme,
    val colors: List<Color>
)

@Composable
fun ThemePreviewCard(theme: ThemeItem, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Preview Colors (Stacked like the images)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy((-15).dp)) {
                    theme.colors.forEachIndexed { index, color ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            color = color,
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = (index * 2).dp
                        ) {}
                    }
                }
            }
            
            // Name and Selection Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = theme.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
