package org.alex.utils;

import org.alex.config.AppConfig;
import org.alex.services.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Facade service responsible for the entire data collection workflow.
 * It orchestrates query generation, web searching, and crawling.
 */
public class WebDataCollector {

    private final LLMService llmService;
    private final AppConfig config;

    public WebDataCollector(LLMService llmService, AppConfig config) {
        this.llmService = llmService;
        this.config = config;
    }

    /**
     * Method that uses the data gathering services in order to scrape and crawl for final content.
     * @return Map of urls to their content
     */

    public Map<String, String> collectData() {
        List<String> urlsToCrawl = getSeedUrls();
        if (urlsToCrawl.isEmpty()) {
            System.out.println("No URLs to process. Halting data collection.");
            return Map.of();
        }

        try (WebScrapingService scraper = new WebScrapingService(new HTMLCleaningService())) {
            WebCrawlerService crawler = new WebCrawlerService(scraper);
            return crawler.crawl(urlsToCrawl, config.crawlDepth(), config.maxCrawlUrlsPerPage());
        } catch (Exception e) {
            System.err.println("A critical error occurred during the crawling phase: " + e.getMessage());
            return Map.of();
        }
    }

    /**
     * Helper method to get URLs, either from cache or by performing a live search.
     */
    private List<String> getSeedUrls() {
        Path cachePath = Paths.get(config.cacheFilePath());
        try {
            if (Files.exists(cachePath) && !Files.readString(cachePath, StandardCharsets.UTF_8).isBlank()) {
                System.out.println("Cache found");
                return Files.readAllLines(cachePath, StandardCharsets.UTF_8);
            } else {
                System.out.println("No cache, generating queries and using google api");
                List<String> searchQueries = llmService.generateSearchQueries(config.researchTopic(), config.llmQueryAmount());

                WebSearchService webSearchService = new WebSearchService(config.googleApikey(), config.cxId());
                List<String> urls = new ArrayList<>();
                for (String query : searchQueries) {
                    urls.addAll(webSearchService.search(query, config.resultsPerQuery()));
                }

                Files.write(cachePath, urls, StandardCharsets.UTF_8);
                return urls;
            }
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}