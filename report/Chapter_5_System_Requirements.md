# CHAPTER 5: SYSTEM REQUIREMENTS

## 5.1 Hardware Requirements
To develop and run the SafeSearchAI application, the following hardware specifications are recommended:

### Developer Environment (PC/Laptop)
- **Processor**: Intel Core i5 or higher (AMD Ryzen 5 or higher).
- **RAM**: Minimum 8 GB (16 GB recommended for smooth Android Studio performance).
- **Storage**: At least 10 GB of free space (for Android Studio, SDKs, and Emulator).
- **Operating System**: Windows 10/11, macOS, or Linux.

### Mobile Device (User Side)
- **Device**: Android smartphone or Tablet.
- **Android Version**: Android 8.0 (API Level 26) or higher.
- **RAM**: Minimum 2 GB.
- **Internet Connectivity**: Required for communicating with Firebase and the Cerebras API.

## 5.2 Software Requirements
The project utilizes a modern development stack to ensure scalability and maintainability.

### Development Tools
- **IDE**: Android Studio (Version Iguana or higher).
- **Language**: Kotlin (Version 1.9.0+).
- **Build System**: Gradle (Kotlin DSL).

### Frameworks and Libraries
- **UI Framework**: Jetpack Compose (for a modern, declarative UI).
- **Asynchronous Processing**: Kotlin Coroutines & Flow.
- **Networking**: Retrofit 2 with OkHttp for REST API communication.
- **JSON Parsing**: Gson or Kotlinx Serialization.
- **Dependency Injection**: Manual or Hilt (optional).

### Backend Services
- **Firebase Auth**: For anonymous user authentication.
- **Firebase Firestore**: For real-time database and message logging.
- **Cerebras Inference API**: For accessing the Llama 3.1 8B Large Language Model.

## 5.3 Network Environment
- **API Access**: The application requires an active internet connection to reach `api.cerebras.ai`.
- **Firebase Connectivity**: Ports 443 (HTTPS) must be open for Firebase Firestore and Authentication.

## 5.4 Functional and Non-Functional Requirements

### Functional Requirements
- The system must classify user queries into three risk levels.
- The system must generate a 5-dimensional reliability score for AI responses.
- The system must display color-coded indicators (Green, Yellow, Red) based on risk and reliability.
- The system must log safety events to the Firebase backend.

### Non-Functional Requirements
- **Latency**: Risk analysis should complete within 2-3 seconds using high-speed inference.
- **Security**: API keys must be handled securely and not exposed in public repositories.
- **Usability**: The chat interface should be intuitive and follow Material 3 design guidelines.
- **Reliability**: The system should gracefully handle API timeouts or network failures.
