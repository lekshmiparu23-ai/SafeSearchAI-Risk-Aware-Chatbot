# CHAPTER 7: SYSTEM IMPLEMENTATION

## 7.1 Development Environment Setup
The implementation phase began with setting up the Android Studio environment. The project was initialized as a "Empty Compose Activity" project. Key dependencies were added to the `build.gradle.kts` file:
- **Firebase BOM**: To manage versions for Auth and Firestore.
- **Retrofit & Gson**: For API communication.
- **Compose Material 3**: For modern UI components.

## 7.2 Firebase Integration
### 1. Firebase Authentication
The system uses Anonymous Authentication to provide a seamless user experience. This allows the application to track unique user sessions and persist chat history without requiring a formal sign-up process.
### 2. Firestore Database
Firestore is used as a NoSQL document database. A `messages` collection stores individual chat entries with fields for:
- `text`: The message content.
- `role`: user or assistant.
- `riskLevel`: SAFE, SENSITIVE, or HARMFUL.
- `reliabilityScore`: The numerical audit result.

## 7.3 Cerebras API Integration
The integration with Cerebras is handled by the `CerebrasApiService`. The implementation uses the Llama 3.1 8B model. To ensure high performance, the `max_tokens` and `temperature` parameters are tuned specifically for classification and auditing tasks (e.g., lower temperature for classification to ensure consistency).

## 7.4 Implementing RiskAnalyzer Logic
The `RiskAnalyzer.kt` is implemented as a Kotlin `object` (singleton). It contains the core prompts used for classification and auditing. The implementation uses **Kotlin Coroutines** (`withContext(Dispatchers.IO)`) to ensure that network calls do not block the main UI thread.

**Algorithm for Risk Analysis**:
1.  Check for deterministic keyword overrides (murder/kill).
2.  Send query to LLM for intent classification.
3.  Apply policy: Standard response for SAFE, Cautious for SENSITIVE, Refusal for HARMFUL.
4.  Send the generated response back to the LLM for a 5-dimensional reliability audit.
5.  Return the consolidated `AIAnalysisResult`.

## 7.5 UI Development with Jetpack Compose
The UI is built using a declarative approach. Key components include:
- `ChatScreen`: A scrollable list of messages using `LazyColumn`.
- `RiskBadge`: A custom component that displays the risk level and color (Green/Yellow/Red).
- `ReliabilityMeter`: A visual indicator showing the audit score and traffic signal.
- `Theme Selection`: Allows users to switch between Light, Dark, and Dynamic color themes.

## 7.6 Testing and Validation during Implementation
Implementation was followed by iterative testing:
- **Unit Testing**: Testing the `RiskAnalyzer` logic with mock API responses.
- **UI Testing**: Ensuring that color-coded badges update correctly when the ViewModel state changes.
- **Integration Testing**: Verifying that messages are correctly saved to Firestore and retrieved upon app restart.

## 7.7 Deployment
The application is packaged as an APK (Android Package) for deployment on physical devices. For the project demonstration, the app is run on an Android 14 Emulator and a physical Pixel device to showcase responsiveness across different screen sizes.
