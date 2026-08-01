# CHAPTER 2: LITERATURE SURVEY

## 2.1 Evolution of Content Moderation
Content moderation has traditionally relied on keyword-based filtering and manual human review. In the context of early internet applications, simple blacklists of "forbidden words" were used to block offensive content. However, these systems were easily bypassed by using synonyms or slightly altering the spelling of prohibited words. Furthermore, they lacked the ability to understand context, leading to many "false positives" where safe content was blocked incorrectly.

## 2.2 Rise of Large Language Models (LLMs)
The introduction of Transformer-based architectures (Vaswani et al., 2017) revolutionized Natural Language Processing (NLP). Models like GPT, BERT, and more recently, Llama, have demonstrated an extraordinary ability to understand semantic context and generate human-like text. In the context of AI safety, LLMs can be used to analyze not just individual words, but the underlying intent of a conversation.

## 2.3 Challenges in AI Safety and Alignment
Despite their capabilities, LLMs face significant safety challenges:
- **Hallucinations**: Generating factually incorrect or nonsensical information.
- **Harmful Bias**: Perpetuating stereotypes found in training data.
- **Jailbreaking**: Techniques used by users to bypass a model's safety constraints.
- **Opacity**: The difficulty in understanding "why" a model makes a specific decision.

Current research in AI Alignment focuses on ensuring that these models act in accordance with human values and safety guidelines. SafeSearchAI contributes to this field by adding an external "Audit Layer" that operates independently of the core model.

## 2.4 Llama 3.1 and the Cerebras Inference Engine
Llama 3.1 is one of the most advanced open-source LLMs available, featuring robust safety alignment. However, running such models in real-time for an Android application requires significant computational power. Cerebras Systems provides specialized AI hardware (the CS-3) and a high-speed inference API that allows for nearly instantaneous processing of LLM requests. This project utilizes the Llama 3.1 8B model via the Cerebras API to ensure that safety auditing does not introduce significant latency for the user.

## 2.5 Real-Time Reliability Auditing
Recent literature has emphasized the importance of not just safety, but "reliability." Reliability auditing involves evaluating a response based on multiple dimensions such as relevance and consistency. By implementing a 5-dimensional scoring system (Relevance, Completeness, Specificity, Clarity, Consistency), SafeSearchAI implements a quantitative approach to AI reliability that is missing in most consumer-facing chat applications.

## 2.6 Summary of Findings
The literature suggests that while LLMs are powerful, they require secondary safety and reliability layers to be used safely in real-world applications. The combination of high-speed inference (Cerebras), advanced open-weights models (Llama 3.1), and a structured auditing framework provides a viable path forward for secure AI-driven communication.
