package org.alex.config;

import io.github.cdimascio.dotenv.Dotenv;

public record AppConfig(
        String openaiApiKey,
        String googleApikey,
        String cxId,
        String researchTopic,
        // How many queries should the agent generate?
        int llmQueryAmount,
        // How many results per query should the google search provide?
        int resultsPerQuery,
        String cacheFilePath,
        // How deep should the crawler go?
        int crawlDepth,
        // How many urls should the crawler access, per page?
        int maxCrawlUrlsPerPage
) {
    public static AppConfig load(){
        Dotenv dotenv = Dotenv.load();

        return new AppConfig(
                dotenv.get("OPENAI_API_KEY"),
                dotenv.get("GOOGLE_SEARCH_API_KEY"),
                dotenv.get("CX_ID"),
                dotenv.get("RESEARCH_TOPIC"),
                Integer.parseInt(dotenv.get("LLM_QUERY_AMOUNT", "3")),
                Integer.parseInt(dotenv.get("RESULTS_PER_QUERY", "3")),
                dotenv.get("CACHE_FILE_PATH", "search_results.txt"),
                Integer.parseInt(dotenv.get("CRAWL_DEPTH", "2")),
                Integer.parseInt(dotenv.get("MAX_CRAWL_URLS_PER_PAGE", "3"))
        );
    }
}