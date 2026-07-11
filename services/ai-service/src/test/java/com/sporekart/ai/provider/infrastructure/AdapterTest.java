package com.sporekart.ai.provider.infrastructure;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AdapterTest {

    private final GeminiAdapter gemini = new GeminiAdapter();
    private final OpenAIAdapter openai = new OpenAIAdapter();
    private final ClaudeAdapter claude = new ClaudeAdapter();
    private final AzureOpenAIAdapter azure = new AzureOpenAIAdapter();
    private final BedrockAdapter bedrock = new BedrockAdapter();
    private final OllamaAdapter ollama = new OllamaAdapter();
    private final MistralAdapter mistral = new MistralAdapter();
    private final LocalLLMAdapter local = new LocalLLMAdapter();

    @Test
    void geminiShouldSupportCorrectType() {
        assertThat(gemini.supports("GEMINI")).isTrue();
        assertThat(gemini.supports("OPENAI")).isFalse();
    }

    @Test
    void geminiShouldGenerateStubResponse() {
        AiRequest request = new AiRequest("test", Map.of());
        AiResponse response = gemini.generate(request);
        assertThat(response.content()).contains("[Gemini stub]");
        assertThat(response.success()).isTrue();
    }

    @Test
    void geminiShouldReturnCapabilities() {
        var caps = gemini.getCapabilities();
        assertThat(caps.getProviderName()).isEqualTo("GEMINI");
        assertThat(caps.supportedModels()).contains("gemini-pro");
        assertThat(caps.supportsStreaming("gemini-pro")).isTrue();
    }

    @Test
    void openaiShouldSupportCorrectType() {
        assertThat(openai.supports("OPENAI")).isTrue();
        assertThat(openai.supports("CLAUDE")).isFalse();
    }

    @Test
    void openaiShouldGenerateStubResponse() {
        AiRequest request = new AiRequest("hello");
        AiResponse response = openai.generate(request);
        assertThat(response.content()).contains("[OpenAI stub]");
    }

    @Test
    void openaiShouldReturnCapabilities() {
        var caps = openai.getCapabilities();
        assertThat(caps.supportsEmbedding("text-embedding-3")).isTrue();
        assertThat(caps.supportsFunctionCalling("gpt-4")).isTrue();
    }

    @Test
    void claudeShouldSupportCorrectType() {
        assertThat(claude.supports("CLAUDE")).isTrue();
    }

    @Test
    void claudeShouldGenerateStubResponse() {
        AiResponse response = claude.generate(new AiRequest("hello"));
        assertThat(response.content()).contains("[Claude stub]");
    }

    @Test
    void azureOpenAIShouldSupportCorrectType() {
        assertThat(azure.supports("AZURE_OPENAI")).isTrue();
    }

    @Test
    void bedrockShouldSupportCorrectType() {
        assertThat(bedrock.supports("BEDROCK")).isTrue();
    }

    @Test
    void ollamaShouldSupportCorrectType() {
        assertThat(ollama.supports("OLLAMA")).isTrue();
    }

    @Test
    void mistralShouldSupportCorrectType() {
        assertThat(mistral.supports("MISTRAL")).isTrue();
    }

    @Test
    void localLLMShouldSupportCorrectType() {
        assertThat(local.supports("LOCAL_LLM")).isTrue();
        assertThat(local.supports("LOCAL")).isTrue();
    }

    @Test
    void allAdaptersShouldBeAvailable() {
        assertThat(gemini.isAvailable()).isTrue();
        assertThat(openai.isAvailable()).isTrue();
        assertThat(claude.isAvailable()).isTrue();
        assertThat(azure.isAvailable()).isTrue();
        assertThat(bedrock.isAvailable()).isTrue();
        assertThat(ollama.isAvailable()).isTrue();
        assertThat(mistral.isAvailable()).isTrue();
        assertThat(local.isAvailable()).isTrue();
    }

    @Test
    void allAdaptersShouldImplementProviderPort() {
        assertThat(gemini.capabilities()).isNotEmpty();
        assertThat(openai.capabilities()).isNotEmpty();
        assertThat(claude.capabilities()).isNotEmpty();
    }
}
