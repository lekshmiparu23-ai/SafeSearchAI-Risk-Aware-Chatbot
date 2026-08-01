# CHAPTER 6: SYSTEM DESIGN

## 6.1 System Architecture
SafeSearchAI follows a Clean Architecture approach with a Model-View-ViewModel (MVVM) pattern. The architecture is divided into three main layers:

1.  **UI Layer (Jetpack Compose)**: Handles user interaction and displays the chat screen, admin dashboard, and risk indicators.
2.  **Domain/Logic Layer (ViewModels & RiskAnalyzer)**: Contains the core business logic, including the multi-stage AI pipeline.
3.  **Data Layer (Firebase & Retrofit)**: Manages communication with external services like Firebase Firestore and the Cerebras AI API.

## 6.2 Flow Diagram (Logical Representation)
The flow of data in the system follows a cyclical path:
1.  **User Input**: Query is entered into the `ChatScreen`.
2.  **ViewModel Processing**: `ChatViewModel` receives the query and initiates the analysis.
3.  **Risk Analysis**: `RiskAnalyzer` executes the multi-stage pipeline (Classification -> Policy -> Audit).
4.  **API Communication**: `CerebrasApiService` handles the network requests to the Llama 3.1 model.
5.  **UI Update**: The `ChatViewModel` updates the UI state with the audited response and color-coded flags.
6.  **Data Persistence**: The transaction is logged to **Firebase Firestore** for audit purposes.

## 6.3 Module Description

### 1. RiskAnalyzer Module
This is the "Brain" of the application. It implements the `analyze()` function which orchestrates the classification and auditing stages. It uses prompt engineering to transform a general-purpose LLM into a specialized safety auditor.

### 2. ChatViewModel Module
This module maintains the state of the chat screen. It manages the list of messages, the visibility of loading indicators, and the logic for triggering the risk analysis. It uses Kotlin Flows to push updates to the UI in a lifecycle-aware manner.

### 3. CerebrasApiService Module
A Retrofit-based interface that defines the communication protocol with the Cerebras inference server. It handles headers (Bearer tokens), request bodies (model parameters), and parses the JSON responses into Kotlin data classes.

### 4. AdminDashboard Module
A restricted UI component that fetches real-time statistics from Firebase, such as the total number of queries analyzed and the distribution of risk levels (Safe vs. Harmful).

## 6.4 Data Flow
| Step | Origin | Destination | Data Sent |
| :--- | :--- | :--- | :--- |
| 1 | User | UI | Raw Text String |
| 2 | UI | ViewModel | Query Object |
| 3 | ViewModel | RiskAnalyzer | Query + API Key |
| 4 | RiskAnalyzer | Cerebras API | Classifier/Auditor Prompts |
| 5 | Cerebras API | RiskAnalyzer | Raw LLM JSON |
| 6 | RiskAnalyzer | ViewModel | `AIAnalysisResult` Object |
| 7 | ViewModel | UI | Color-coded Chat Message |

## 6.5 Security Design
- **API Key Masking**: API keys are injected via build configurations or secure constants to prevent accidental leakage.
- **Input Sanitization**: User input is trimmed and checked for illegal characters before being sent to the AI network.
- **Fail-Safe Mechanism**: If the AI network is unreachable, the system defaults to a "Moderate Reliability" warning with a local error message, ensuring the user is never left without feedback.
