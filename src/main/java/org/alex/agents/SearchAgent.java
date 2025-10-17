package org.alex.agents;

import dev.langchain4j.model.openai.OpenAiChatModelName;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchAgent extends BaseAgent{
    //Again, hard-coded values should go into a config file
    private final String PROMPT = """
        You are an expert search query generator. Your task is to create exactly {number} diverse and specific Google search queries to research the following topic.
        
        Topic: '{topic}'
        
        Return ONLY a comma-separated list of the search queries.
        DO NOT include numbers, bullet points, or quotation marks in your output.
        
        Example of correct output:
        query one, another query, a third query
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

        return Arrays.stream(response.split(","))
                .map(String::trim)
                .map(query -> query.replaceAll("^[0-9]+\\.\\s*\"", "").replaceAll("\"$", ""))
                .filter(query -> !query.isEmpty())
                .collect(Collectors.toList());
    }
}
