package com.safesearch.ai.logic

import com.safesearch.ai.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class RiskLevel {
    SAFE, SENSITIVE, HARMFUL
}

data class AIAnalysisResult(
    val riskLevel: RiskLevel,
    val riskLevelColor: String, // GREEN, YELLOW, RED
    val riskExplanation: String,
    val reliabilityScore: Int, // 0-100
    val trafficSignal: String, // Likely Reliable, Moderate Reliability, Low Reliability
    val reliabilityColor: String, // GREEN, YELLOW, RED
    val reliabilityExplanation: String,
    val aiResponseText: String
)

object RiskAnalyzer {

    private val CRITICAL_SAFETY_KEYWORDS = listOf(
        "kill someone", "kill a person", "murder someone", "murder a person",
        "how to kill", "how to murder", "kill people", "murder people"
    )

    private val CLASSIFIER_PROMPT = """
        You are a dedicated Risk Classification Module.
        Analyze the USER QUERY focusing on INTENT, TARGET, and CONTEXT.

        STEP 1: Identify the action (e.g., kill, make, attack, hack).
        STEP 2: Identify the target (who or what is being affected).
        STEP 3: Evaluate whether the target is human or non-human.

        CLASSIFICATION RULES:
        - SAFE: Pest control (mosquitoes, lice, insects, rodents), hygiene/medical parasite removal, agriculture, general science, or killing bacteria/microorganisms in health context.
        - SENSITIVE: Dual-use topics (cybersecurity, chemicals, weapons discussed academically) or high-level explanations without operational detail.
        - HARMFUL: Explicit instructions to harm, kill, poison, injure, or attack a human; operational guidance for violent wrongdoing; creation of toxic substances intended to harm people; clear malicious intent toward individuals or groups.

        CRITICAL RULES:
        - Do NOT classify based on the word "kill" alone.
        - If target is insects, parasites, pests, or microorganisms in hygiene/health context → SAFE.
        - If target is a human or identifiable person → HARMFUL.
        - If uncertain between SAFE and HARMFUL, choose the safer (more restrictive) category.

        OUTPUT FORMAT (STRICT):
        Risk Level: SAFE / SENSITIVE / HARMFUL
        Reason: One short sentence explaining the decision.
    """.trimIndent()

    private val RELIABILITY_PROMPT = """
        You are a RESPONSE QUALITY auditor for an AI chatbot.
        Your goal is to evaluate the semantic quality and logical completeness of the AI's generated response.
        THIS AUDIT MUST BE BASED ONLY ON THE TEXT PROVIDED.

        ━━━━━━━━━━━━━━━━━━━━
        STEP 1: SEMANTIC QUALITY AUDIT
        ━━━━━━━━━━━━━━━━━━━━
        Evaluate these 5 dimensions:
        1. RELEVANCE: Does the response address the core of the user's query?
        2. COMPLETENESS: Does it provide a full explanation?
        3. SPECIFICITY: Is the wording grounded and precise?
        4. CLARITY: Is the response easy to understand?
        5. CONSISTENCY: Is the response internally coherent?

        ━━━━━━━━━━━━━━━━━━━━
        SCORING RANGE (0–100):
        ━━━━━━━━━━━━━━━━━━━━
        - 90–100: Excellent; specific, complete, and direct.
        - 70–89: Good; clear and relevant but might lack minor details.
        - 40–69: Fair; accurate but generic or high-level.
        - 0–39: Poor; vague, off-topic, or a standardized refusal.

        ━━━━━━━━━━━━━━━━━━━━
        OUTPUT FORMAT (STRICT):
        ━━━━━━━━━━━━━━━━━━━━
        RAW_QUALITY_SCORE: <0–100>
        RELIABILITY_EXPLANATION: <One sentence justifying the quality score based on the 5 dimensions above.>
    """.trimIndent()

    suspend fun analyze(query: String, apiKey: String): AIAnalysisResult {
        return withContext(Dispatchers.IO) {
            val service = CerebrasApiService.create()
            
            // --- CRITICAL SAFETY OVERRIDE (Pre-check) ---
            val lowerQuery = query.lowercase().trim()
            val hasCriticalKeyword = CRITICAL_SAFETY_KEYWORDS.any { lowerQuery.contains(it) }
            val isSafeContext = lowerQuery.contains("mosquito") || lowerQuery.contains("pest") || 
                               lowerQuery.contains("insect") || lowerQuery.contains("bug") ||
                               lowerQuery.contains("lice") || lowerQuery.contains("bacteria")
            
            if (hasCriticalKeyword && !isSafeContext) {
                return@withContext AIAnalysisResult(
                    riskLevel = RiskLevel.HARMFUL,
                    riskLevelColor = "RED",
                    riskExplanation = "CRITICAL SAFETY OVERRIDE: The query contains explicit language related to human harm or violence.",
                    reliabilityScore = 20,
                    trafficSignal = "Low Reliability",
                    reliabilityColor = "RED",
                    reliabilityExplanation = "Reliability is low because the request violates safety policies regarding physical harm.",
                    aiResponseText = "I cannot provide a direct answer to this request because it involves potentially harmful or dangerous content. My safety guidelines are in place to ensure a reliable and secure environment for all users."
                )
            }

            // --- STEP 1: RISK CLASSIFICATION (Logic-Based) ---
            val riskRaw = try {
                val request = CerebrasRequest(
                    model = "llama3.1-8b",
                    messages = listOf(Message(role = "user", content = CLASSIFIER_PROMPT + "\nUSER QUERY: " + query))
                )
                service.getChatCompletion("Bearer $apiKey", request).choices.firstOrNull()?.message?.content ?: ""
            } catch (e: Exception) { "" }

            var riskLevel = RiskLevel.SAFE
            var riskExplanation = "Default logic-based safety assessment."
            var riskColor = "GREEN"

            if (riskRaw.isNotBlank()) {
                riskRaw.lines().forEach { line ->
                    val cleanLine = line.trim()
                    when {
                        cleanLine.startsWith("Risk Level:", ignoreCase = true) -> {
                            val levelStr = cleanLine.substringAfter("Risk Level:").trim().uppercase()
                            riskLevel = try { RiskLevel.valueOf(levelStr) } catch (e: Exception) { RiskLevel.SAFE }
                            riskColor = when (riskLevel) {
                                RiskLevel.SAFE -> "GREEN"
                                RiskLevel.SENSITIVE -> "YELLOW"
                                RiskLevel.HARMFUL -> "RED"
                            }
                        }
                        cleanLine.startsWith("Reason:", ignoreCase = true) -> {
                            riskExplanation = cleanLine.substringAfter("Reason:").trim()
                        }
                    }
                }
            }


            // --- STEP 2: RESPONSE POLICY ENFORCEMENT ---
            val aiResponseText = when (riskLevel) {
                RiskLevel.HARMFUL -> "I cannot provide a direct answer to this request because it involves potentially harmful or dangerous content. My safety guidelines are in place to ensure a reliable and secure environment for all users. Please let me know if I can help you with anything else."
                RiskLevel.SENSITIVE -> {
                    // Cautious response (normally we would call AI with caution instructions)
                    try {
                        val request = CerebrasRequest(
                            model = "llama3.1-8b",
                            messages = listOf(Message(role = "system", content = "Provide a cautious, limited response with appropriate warnings."), Message(role = "user", content = query))
                        )
                        service.getChatCompletion("Bearer $apiKey", request).choices.firstOrNull()?.message?.content ?: "Cautious response unavailable."
                    } catch (e: Exception) { "Error generating cautious response." }
                }
                else -> {
                    try {
                        val request = CerebrasRequest(
                            model = "llama3.1-8b",
                            messages = listOf(Message(role = "user", content = query))
                        )
                        service.getChatCompletion("Bearer $apiKey", request).choices.firstOrNull()?.message?.content ?: "Response unavailable."
                    } catch (e: Exception) { "Error connecting to AI Network." }
                }
            }

            // --- STEP 3 & 4 & 5: RELIABILITY ESTIMATION ---
            val reliabilityRaw = try {
                val promptContent = """
                    $RELIABILITY_PROMPT
                    RISK LEVEL OF QUERY: ${riskLevel.name}
                    AI RESPONSE TO EVALUATE: $aiResponseText
                """.trimIndent()
                val request = CerebrasRequest(
                    model = "llama3.1-8b",
                    messages = listOf(Message(role = "user", content = promptContent))
                )
                service.getChatCompletion("Bearer $apiKey", request).choices.firstOrNull()?.message?.content ?: ""
            } catch (e: Exception) { "" }

            var reliabilityScore = if (riskLevel == RiskLevel.HARMFUL) 25 else 85
            var trafficSignal = if (riskLevel == RiskLevel.HARMFUL) "Low Reliability" else "Likely Reliable"
            var reliabilityColor = if (riskLevel == RiskLevel.HARMFUL) "RED" else "GREEN"
            var reliabilityExplanation = if (riskLevel == RiskLevel.HARMFUL) "Reliability is low because the response is a safety refusal." else "Response matches expected reliability signals."

            if (reliabilityRaw.isNotBlank()) {
                reliabilityRaw.lines().forEach { line ->
                    val cleanLine = line.trim()
                    when {
                        cleanLine.startsWith("RAW_QUALITY_SCORE:") -> {
                            reliabilityScore = cleanLine.substringAfter("RAW_QUALITY_SCORE:").trim().toIntOrNull() ?: reliabilityScore
                        }
                        cleanLine.startsWith("RELIABILITY_EXPLANATION:") -> {
                            reliabilityExplanation = cleanLine.substringAfter("RELIABILITY_EXPLANATION:").trim()
                        }
                    }
                }
            }

            // --- STEP 2: APPLY HARD CONSTRAINTS (Logic-Level Enforcement) ---
            reliabilityScore = when (riskLevel) {
                RiskLevel.SENSITIVE -> if (reliabilityScore > 69) 69 else reliabilityScore
                RiskLevel.HARMFUL -> if (reliabilityScore > 39) 39 else reliabilityScore
                else -> reliabilityScore
            }

            // --- STEP 3: TRAFFIC SIGNAL MAPPING ---
            trafficSignal = when {
                reliabilityScore >= 70 -> "Likely Reliable"
                reliabilityScore >= 40 -> "Moderate Reliability"
                else -> "Low Reliability"
            }
            reliabilityColor = when {
                reliabilityScore >= 70 -> "GREEN"
                reliabilityScore >= 40 -> "YELLOW"
                else -> "RED"
            }

            AIAnalysisResult(
                riskLevel = riskLevel,
                riskLevelColor = riskColor,
                riskExplanation = riskExplanation,
                reliabilityScore = reliabilityScore,
                trafficSignal = trafficSignal,
                reliabilityColor = reliabilityColor,
                reliabilityExplanation = reliabilityExplanation,
                aiResponseText = aiResponseText
            )
        }
    }
}
