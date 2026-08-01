# CHAPTER 3: EXISTING SYSTEM

## 3.1 Overview of Current AI Chat Systems
The current ecosystem of AI chat applications (such as OpenAI's ChatGPT, Google's Gemini, and Anthropic's Claude) relies on a combination of Reinforcement Learning from Human Feedback (RLHF) and internal safety layers. These systems are designed to refuse harmful queries and provide helpful responses. In many academic and small-scale projects, safety is often implemented using basic "Keyword Blacklists" where specific words (e.g., "kill", "bomb", "hack") trigger an automatic refusal.

## 3.2 System Workflow of Existing Solutions
1.  **User Input**: User types a query.
2.  **Internal Filtering**: The query is checked against a database of prohibited terms or processed by a safety-aligned model.
3.  **Response Generation**: If the query is deemed safe, a response is generated.
4.  **Output**: The user receives the response without any additional safety context or reliability information.

## 3.3 Limitations of Existing Systems
The existing systems face several significant challenges:
1.  **Lack of Transparency (Opacity)**: Users are often presented with a generic "I cannot answer this" message without a specific reason or explanation for the refusal.
2.  **Binary Filtering**: Most systems use a "pass/fail" approach to safety. They do not distinguish between "SENSITIVE" topics (which require caution) and "HARMFUL" topics (which require a hard refusal).
3.  **Absence of Reliability Metrics**: There is no real-time auditing of the generated response. A response might be factually incorrect or inconsistent, yet the user has no way of knowing the "confidence" or "reliability" of that specific answer.
4.  **Over-Refusal**: Due to a lack of deep contextual understanding, keyword-based systems often block safe queries (e.g., "how to kill a mosquito") simply because they contain a flagged word.
5.  **Disconnected Experience**: Safety analysis is performed "behind the scenes," leaving the user disconnected from the security process.

## 3.4 Summary of Challenges
| Challenge | Impact on User |
| :--- | :--- |
| **Manual Interpretation** | Users must guess why a query was rejected. |
| **No Visual Aids** | Lack of color-coded risk indicators makes it hard to quickly assess safety. |
| **No Real-Time Alerts** | Users may interact with unreliable information without warning. |
| **Binary Safety Logic** | High risk of either too much restriction or not enough. |

The existing system model fails to provide the advanced, transparent auditing required for a safe and reliable AI interaction environment, particularly in sensitive educational or research contexts.
