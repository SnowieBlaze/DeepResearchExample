package org.alex.agents;

import dev.langchain4j.model.openai.OpenAiChatModelName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResearchAgent extends BaseAgent {

    private final String MAP_PROMPT = """
            You are a summarization agent. Your task is to read the following text and provide a concise, factual and detailed summary.
            Extract all the key points, arguments, and conclusions. The summary should be very dense with information.
            
            Article Text: {content}
            """;
    private final String REDUCE_PROMPT = """
            You are the best researcher in the world. You have been provided with a list of summaries for a topic.
            Your task is to synthesize this information into a single comprehensive, well-structured, factual report.
            
            Do not list summaries. Weave their information together, and form a coherent narrative. The report should have an introduction, key themes supported by facts from the summaries and also a concluding paragraph.
            I want you to clearly separate the introduction, the content(in which you will clearly separate key points/paragraphs), the conclusion.
            
            Original Topic: '{topic}'
            
            Summaries:
            
            {summaries}
            """;

    public ResearchAgent(String apiKey) {
        super(apiKey, OpenAiChatModelName.GPT_4_TURBO_PREVIEW, 0.5, 4000);
    }

    /**
     * Maps a list of page content to a map of content and its summary.
     * @param pageContent - list of page content to map
     * @return - map of content and its summary
     */

    public Map<String, String> map(List<String> pageContent){
        Map<String, String> ret = new HashMap<>();

        for(String content: pageContent){
            String truncatedContent = content.substring(0, Math.min(content.length(), 1000));
            String prompt = MAP_PROMPT.replace("{content}", truncatedContent);
            String response = model.chat(prompt);
            ret.put(content, response);
        }

        return ret;
    }

    /**
     * Combines the summaries of each page into a single big summary.
     * @param mappedContent - map of content and its summary
     * @return - all summaries combined.
     */
    public String mapReduce(Map<String, String> mappedContent){
        int curr = 1;
        StringBuilder sb = new StringBuilder();

        for(String key: mappedContent.keySet()){
            sb.append("Summary of article ").append(curr).append(": ").append(mappedContent.get(key)).append("\n");
        }

        return sb.toString();
    }

    /**
     * Combines the summaries into a final report.
     * @param topic - topic of the report
     * @param summaries - list of summaries to combine
     * @return - final report, in string format
     */
    public String reduce(String topic, String summaries){
        String prompt = REDUCE_PROMPT.replace("{topic}", topic).replace("{summaries}", summaries);
        return model.chat(prompt);
    }
}
