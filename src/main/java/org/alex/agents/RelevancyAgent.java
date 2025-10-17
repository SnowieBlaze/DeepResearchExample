package org.alex.agents;

import dev.langchain4j.model.openai.OpenAiChatModelName;

public class RelevancyAgent extends BaseAgent{
    //Again, hard-coded values should go into a config file
    private final String PROMPT = """
        You are a relevancy agent, that specializes in deciding if a piece of text is relevant or not.
        Your job is to determine if the following text is relevant to the topic.
        You should ALWAYS respond with a single word: either 'relevant' or 'irrelevant'.
        Research Topic: '{topic}'
        Text: '{text}'
    """;

    public RelevancyAgent(String apiKey){
        super(apiKey, OpenAiChatModelName.GPT_3_5_TURBO, 0.0, 5);
    }

    /**
     * Checks if a piece of text is relevant to a given topic.
     * @param topic - topic to check for
     * @param text - text to check if relevant to topic
     * @return - true if relevant, false otherwise
     */
    public boolean isRelevant(String topic, String text){
        String prompt = PROMPT.replace("{topic}", topic).replace("{text}", text);
        String response = model.chat(prompt);
        return response.trim().equalsIgnoreCase("relevant");
    }
}
