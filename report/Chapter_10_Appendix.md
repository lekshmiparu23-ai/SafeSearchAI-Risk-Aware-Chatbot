# CHAPTER 10: APPENDIX

## 10.1 Source Code (Key Modules)

### 1. RiskAnalyzer.kt
```kotlin
// Simplified version of the core risk analysis logic
object RiskAnalyzer {
    private val CLASSIFIER_PROMPT = """
        Analyze the USER QUERY focusing on INTENT, TARGET, and CONTEXT.
        CLASSIFICATION RULES:
        - SAFE: Pest control, hygiene, general science.
        - SENSITIVE: Dual-use topics, academic discussions.
        - HARMFUL: Explicit harm to humans, illegal guidance.
    """.trimIndent()

    suspend fun analyze(query: String, apiKey: String): AIAnalysisResult {
        // Multi-stage pipeline implementation
        // 1. Classification
        // 2. Policy Enforcement
        // 3. Reliability Audit
    }
}
```

### 2. ChatViewModel.kt
```kotlin
class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun sendMessage(text: String) {
        // Logic to trigger analysis and update UI state
    }
}
```

## 10.2 Screenshots (Visual Representation)

### 1. Login and Theme Selection
*(Placeholder for screenshot showing the anonymous login screen and the dynamic theme selection menu.)*

### 2. Safe Chat Interaction (Green)
*(Placeholder for screenshot showing a safe query like "How to cook pasta" with a Green Reliability Badge.)*

### 3. Sensitive Topic Warning (Yellow)
*(Placeholder for screenshot showing a query about "hacking" with a Yellow Caution Badge and a cautious AI response.)*

### 4. Harmful Intent Refusal (Red)
*(Placeholder for screenshot showing a query about "human harm" with a Red Risk Badge and a standardized safety refusal.)*

### 5. Admin Dashboard
*(Placeholder for screenshot showing the Admin statistics page with query counts and risk distributions.)*
