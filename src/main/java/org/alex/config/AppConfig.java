package org.alex.config;

import io.github.cdimascio.dotenv.Dotenv;

public record AppConfig(
        String openaiApiKey,
        String googleApikey,
        String cxId,
        String researchTopic,
        int llmQueryAmount,
        int resultsPerQuery,
        String cacheFilePath,
        int crawlDepth,
        int maxCrawlUrlsPerPage
) {
    public static AppConfig load(){
        Dotenv dotenv = Dotenv.load();

        return new AppConfig(
                dotenv.get("OPENAI_API_KEY"),
                dotenv.get("GOOGLE_SEARCH_API_KEY"),
                dotenv.get("CX_ID"),
                "How to build a deep research agent",
                3,
                3,
                dotenv.get("CACHE_FILE_PATH"),
                2,
                3
        );
    }
}
