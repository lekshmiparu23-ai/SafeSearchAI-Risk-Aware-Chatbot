# CHAPTER 4: PROPOSED SYSTEM

## 4.1 System Overview
The proposed system, **SafeSearchAI**, is designed to provide a transparent and robust safety layer for AI-user interactions. The core of the system is a **Multi-Stage AI Risk and Reliability Pipeline** that analyzes every query and response before it is displayed to the user. By combining deterministic logic (keyword overrides) with probabilistic reasoning (LLM-based classification), the system achieves a high degree of accuracy and safety.

## 4.2 Key Components
1.  **Android Client (Kotlin + Jetpack Compose)**: The user interface where queries are entered and audited responses are displayed with color-coded risk indicators.
2.  **Risk Analyzer Module**: The central logic unit that coordinates the multi-stage analysis using the Cerebras API.
3.  **Cerebras API (Llama 3.1 8B)**: The high-speed inference engine used for intent classification and reliability auditing.
4.  **Firebase Firestore**: Used for storing chat history and safety logs for the Admin Dashboard.
5.  **Firebase Authentication**: Provides anonymous login to ensure user privacy while maintaining a unique session.

## 4.3 Functionalities (The Multi-Stage Pipeline)
The system operates through a sequential 5-step pipeline:

### Stage 1: Critical Safety Override (Deterministic)
Before any AI processing occurs, the system checks for explicit high-risk keywords (e.g., "murder", "kill person"). However, it intelligently ignores these flags if a safe context (e.g., "mosquito", "pest control") is detected in the same query.

### Stage 2: Intent-Based Risk Classification (Probabilistic)
The query is sent to the Llama 3.1 model with a specialized "Classifier Prompt." The model evaluates the **Intent**, **Target**, and **Context** to categorize the query as:
- **SAFE**: Benign requests or hygiene/pest control contexts.
- **SENSITIVE**: Dual-use topics or complex academic discussions.
- **HARMFUL**: Explicit instructions to harm humans or illegal operational guidance.

### Stage 3: Response Policy Enforcement
Based on the classification, the system decides on an output policy:
- **HARMFUL**: Displays a standardized safety refusal message.
- **SENSITIVE**: Generates a cautious, limited response with appropriate warnings.
- **SAFE**: Generates a standard, helpful response.

### Stage 4: Reliability Auditing (5-Dimensional Scoring)
The generated response is audited across five semantic dimensions:
1.  **Relevance**: Does the response address the core query?
2.  **Completeness**: Does it provide a full explanation?
3.  **Specificity**: Is the wording grounded and precise?
4.  **Clarity**: Is it easy to understand?
5.  **Consistency**: Is it internally coherent?

### Stage 5: Traffic Signal Mapping
The reliability score (0-100) is mapped to a visual "Traffic Signal":
- **Green (70-100)**: Likely Reliable.
- **Yellow (40-69)**: Moderate Reliability.
- **Red (0-39)**: Low Reliability (or Safety Refusal).

## 4.4 Security and Privacy Considerations
- **Anonymous Sessions**: No personal data is collected or stored.
- **Data Sanitization**: Internal logs are cleaned of any potentially identifying information.
- **Hard Constraints**: Safety refusals are enforced at the logic level, preventing any "jailbreak" from bypassing the safety message.

## 4.5 Implementation Plan Summary
The system is implemented in a modular fashion, allowing for easy updates to the AI model or the auditing dimensions. The use of Kotlin Coroutines ensures that this complex pipeline runs asynchronously, maintaining a smooth 60fps UI experience on the Android device.
