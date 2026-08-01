package com.safesearch.ai.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.safesearch.ai.logic.AIAnalysisResult
object FirebaseRepo {
    val db = Firebase.firestore

    fun logQuery(query: String, response: String, analysis: AIAnalysisResult) {
        val data = hashMapOf(
            "query" to query,
            "response" to response,
            "riskLevel" to analysis.riskLevel.name,
            "riskExplanation" to analysis.riskExplanation,
            "reliabilityScore" to analysis.reliabilityScore,
            "trafficSignal" to analysis.trafficSignal,
            "reliabilityColor" to analysis.reliabilityColor,
            "reliabilityExplanation" to analysis.reliabilityExplanation,
            "timestamp" to System.currentTimeMillis()
        )
        
        db.collection("queries")
            .add(data)
            .addOnSuccessListener { _ -> 
                println("DocumentSnapshot added with ID") 
            }
            .addOnFailureListener { e -> 
                println("Error adding document: $e") 
            }
    }
}
