package org.alex.agents;

import dev.langchain4j.model.openai.OpenAiChatModelName;

import java.util.ArrayList;
import java.util.List;

public class SearchAgent extends BaseAgent{
    //Again, hard-coded values should go into a config file
    private final String PROMPT = """
        You are an expert search query generator. Your task is to create exactly {number} diverse and specific Google search queries to research the following topic.
        
        Topic: '{topic}'
        
        Return ONLY a new line-separated list of the search queries and nothing else.
        Example: 
         query one
         another query
         a third query
        """;



    public SearchAgent(String apiKey){
        super(apiKey, OpenAiChatModelName.GPT_3_5_TURBO, 0.5, 200);
    }

    /**
     * Generates a list of search queries for a given topic.
     * @param number - number of search queries to generate
     * @param topic - topic to search for
     * @return - list of search queries
     */
    public List<String> generateSearches(Integer number, String topic){
        String prompt = PROMPT.replace("{number}", number.toString()).replace("{topic}", topic);
        String response = model.chat(prompt);
        String[] lines = response.split("\n");

        return new ArrayList<>(List.of(lines));
    }
}
