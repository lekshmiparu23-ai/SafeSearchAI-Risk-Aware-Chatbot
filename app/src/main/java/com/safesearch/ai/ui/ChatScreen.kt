package com.safesearch.ai.ui

import com.safesearch.ai.BuildConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.safesearch.ai.logic.AIAnalysisResult
import com.safesearch.ai.logic.ChatViewModel
import com.safesearch.ai.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, vm: ChatViewModel = viewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val history by vm.history.collectAsState()
    
    var showDeleteDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxHeight().width(300.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    // Drawer Header
                    Text(
                        "History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    
                    Divider(color = Color.White.copy(alpha = 0.1f))

                    // New Chat Button
                    NavigationDrawerItem(
                        label = { Text("New Chat") },
                        selected = false,
                        onClick = {
                            vm.startNewChat()
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // History List
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(history) { convo ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        vm.loadConversation(convo)
                                        scope.launch { drawerState.close() }
                                    },
                                color = if (vm.conversationId == convo.id) AI_MediumGrey else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = convo.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Divider(color = Color.White.copy(alpha = 0.1f))

                    // Clear Conversations
                    TextButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clear conversations", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 14.sp)
                    }
                }
            }
        }
    ) {
        MainChatContent(navController, vm, onMenuClick = { scope.launch { drawerState.open() } })
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Clear all conversations?") },
            text = { Text("This will permanently delete your entire chat history.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.clearHistory()
                    showDeleteDialog = false
                }) {
                    Text("Clear", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun MainChatContent(navController: NavController, vm: ChatViewModel, onMenuClick: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val apiKey = BuildConfig.CERBERUS_API_KEY

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- Header ---
        var showMenu by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape,
                modifier = Modifier.size(40.dp).clickable { onMenuClick() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Risk Analysis",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    DropdownMenuItem(
                        text = { Text("Admin Dashboard") },
                        onClick = {
                            showMenu = false
                            navController.navigate("admin")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Theme Settings") },
                        onClick = {
                            showMenu = false
                            navController.navigate("choose_theme")
                        }
                    )
                }
            }
        }

        // --- Chat Content Area ---
        Box(modifier = Modifier.weight(1f)) {
            if (vm.messages.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "What can I help with?",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(vm.messages) { item ->
                        if (item.isUser) {
                            UserMessageBubble(item.text)
                        } else {
                                item.analysis?.let { a ->
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        RiskLevelCard(a.riskLevel, a.riskLevelColor, a.riskExplanation)
                                        ResponseCoreCard(a.aiResponseText)
                                        ReliabilityScoreBar(a.reliabilityScore, a.trafficSignal, a.reliabilityColor)
                                        ReasonCard(a.reliabilityExplanation)
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                        }
                    }
                    if (vm.isLoading) {
                        item {
                            Text(
                                "Analyzing...", 
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), 
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- Footer ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape,
                modifier = Modifier.size(44.dp).clickable { vm.startNewChat() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape,
                modifier = Modifier.weight(1f).height(44.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
                    androidx.compose.foundation.text.BasicTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp),
                        decorationBox = { innerTextField ->
                            if (query.isEmpty()) Text("Ask AI", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 16.sp)
                            innerTextField()
                        },
                        singleLine = true
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier.size(44.dp),
                enabled = !vm.isLoading && query.isNotBlank(),
                onClick = {
                    val userText = query
                    query = ""
                    vm.sendMessage(userText, apiKey)
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
fun UserMessageBubble(text: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.widthIn(max = 300.dp)) {
            Text(text = text, modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 15.sp)
        }
    }
}

@Composable
fun ReliabilityScoreBar(score: Int, level: String, colorStr: String) {
    val color = when (colorStr) {
        "GREEN" -> RiskSafe
        "YELLOW" -> RiskMedium
        "RED" -> RiskHigh
        else -> Color.Gray
    }
    
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TrafficLightDot(color, true)
                TrafficLightDot(if (score < 75) color else Color.Gray, score < 75)
                TrafficLightDot(if (score < 40) color else Color.Gray, score < 40)
            }
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                color = Color.Black.copy(alpha = 0.3f),
                shape = RoundedCornerShape(4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "RELIABILITY: $score%",
                    color = color,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = level.uppercase(),
                color = color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }
}

// ReliabilityScoreBar replaces old evaluation components

@Composable
fun TrafficLightDot(color: Color, isActive: Boolean) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(if (isActive) color else Color(0xFF333333))
            .then(if (isActive) Modifier.border(2.dp, color.copy(alpha = 0.2f), CircleShape) else Modifier)
    )
}

@Composable
fun ReasonCard(reason: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = reason,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 13.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            modifier = Modifier.padding(16.dp),
            lineHeight = 18.sp
        )
    }
}

@Composable
fun RiskLevelCard(riskLevel: com.safesearch.ai.logic.RiskLevel, colorStr: String, reason: String) {
    val bgColor = when (colorStr) {
        "GREEN" -> Color(0xFF1B2E1E).copy(alpha = 0.8f) // Dark Green
        "YELLOW" -> Color(0xFF2D240E).copy(alpha = 0.8f) // Dark Yellow/Brown
        "RED" -> Color(0xFF2D1010).copy(alpha = 0.8f) // Dark Red
        else -> MaterialTheme.colorScheme.surface
    }
    
    val textColor = when (colorStr) {
        "GREEN" -> RiskSafe
        "YELLOW" -> Color(0xFFFDD835)
        "RED" -> Color(0xFFE57373)
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (riskLevel == com.safesearch.ai.logic.RiskLevel.SAFE) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "RISK LEVEL: ${riskLevel.name}",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = reason,
                color = textColor.copy(alpha = 0.8f),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ResponseCoreCard(response: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "RESPONSE CORE",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = response,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }
    }
}
