package org.alex.services;

import org.alex.agents.RelevancyAgent;
import org.alex.agents.ResearchAgent;
import org.alex.agents.SearchAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LLMService {

    private final SearchAgent searchAgent;
    private final RelevancyAgent relevancyAgent;
    private final ResearchAgent researchAgent;

    /**
     * Initializes all agents used by the LLMService.
     * @param openaiApiKey - OpenAI API key
     */
    public LLMService(String openaiApiKey) {
        this.searchAgent = new SearchAgent(openaiApiKey);
        this.relevancyAgent = new RelevancyAgent(openaiApiKey);
        this.researchAgent = new ResearchAgent(openaiApiKey);
    }

    /**
     * Generates a list of search queries for a given topic.
     * @param topic - topic to search for
     * @param numberOfQueries - number of search queries to generate
     * @return - list of search queries
     */
    public List<String> generateSearchQueries(String topic, int numberOfQueries) {
        return searchAgent.generateSearches(numberOfQueries, topic);
    }

    /**
     * Filters the crawled data to only include relevant content.
     * @param topic - topic to search for
     * @param crawledData - map of urls to crawled content
     * @return - list of relevant page contents
     */
    public List<String> filterRelevantContent(String topic, Map<String, String> crawledData) {
        List<String> relevantContents = new ArrayList<>();
        for (Map.Entry<String, String> entry : crawledData.entrySet()) {
            String content = entry.getValue();
            String snippet = content.substring(0, Math.min(content.length(), 1500));

            if (relevancyAgent.isRelevant(topic, snippet)) {
                relevantContents.add(content);
            }
        }
        return relevantContents;
    }

    /**
     * Synthesizes a final report for a given topic and list of relevant contents.
     * @param topic - topic to summarize
     * @param relevantContents - list of relevant contents to summarize
     * @return - final report, in string format
     */
    public String generateReport(String topic, List<String> relevantContents) {
        Map<String, String> mappedSummaries = researchAgent.map(relevantContents);

        String summaries = researchAgent.mapReduce(mappedSummaries);
        String combinedSummaries = researchAgent.reduce(topic, summaries);

        return researchAgent.reduce(topic, combinedSummaries);
    }
}