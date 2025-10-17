package org.alex.agents;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;

public abstract class BaseAgent {

    protected final OpenAiChatModel model;

    public BaseAgent(String apiKey, OpenAiChatModelName modelName, double temperature, int maxTokens){
        if(apiKey == null) throw new IllegalArgumentException("API key cannot be null");
        if(modelName == null) throw new IllegalArgumentException("Model name cannot be null");

        this.model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .build();
    }
}
