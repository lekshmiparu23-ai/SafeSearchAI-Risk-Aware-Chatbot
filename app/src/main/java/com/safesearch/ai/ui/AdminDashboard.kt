package com.safesearch.ai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.safesearch.ai.ui.theme.RiskHigh
import com.safesearch.ai.ui.theme.RiskMedium
import com.safesearch.ai.ui.theme.RiskSafe

@Composable
fun AdminDashboard(navController: NavController) {
    // Mock Data for demonstration
    val safeCount = 120
    val warningCount = 30
    val unsafeCount = 10
    val total = safeCount + warningCount + unsafeCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Admin Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Total Queries Processed: $total", color = MaterialTheme.colorScheme.onBackground)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        StatRow("Safe (Green)", safeCount, RiskSafe)
        StatRow("Warning (Yellow)", warningCount, RiskMedium)
        StatRow("Unsafe (Red)", unsafeCount, RiskHigh)
        
        Spacer(modifier = Modifier.height(32.dp))
        Text("Safety Trends", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(16.dp))
        
        // Simple Bar Chart
        SimpleBarChart(safeCount, warningCount, unsafeCount)
    }
}

@Composable
fun StatRow(label: String, count: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = color, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text("$count", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun SimpleBarChart(safe: Int, warning: Int, unsafe: Int) {
    val max = maxOf(safe, warning, unsafe).toFloat()
    
    Row(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        Bar(height = (safe / max), color = RiskSafe, label = "Safe")
        Bar(height = (warning / max), color = RiskMedium, label = "Warn")
        Bar(height = (unsafe / max), color = RiskHigh, label = "Risk")
    }
}

@Composable
fun Bar(height: Float, color: Color, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Height is relative to 1.0 (100%)
        // We multiply by 150.dp just for visual scaling in the fixed 200.dp container
        Box(
            modifier = Modifier
                .size(width = 40.dp, height = (height * 150).dp)
                .background(color, MaterialTheme.shapes.small)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.bodySmall)
    }
}
