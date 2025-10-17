package org.alex.services;

import org.alex.records.ScrapeRecord;

import java.util.*;

public class WebCrawlerService {
    private final WebScrapingService webScrapingService;

    public WebCrawlerService(WebScrapingService webScrapingService) {
        this.webScrapingService = webScrapingService;
    }

    /**
     * Crawls a website in breadth-first manner.
     * The crawler will visit each url once and then visit each link found on the page, until the depth limit is reached.
     * @param startUrls - list of urls to start crawling from
     * @param maxDepth - maximum depth to crawl to
     * @param maxUrlsPerPage - maximum number of urls to visit per page
     * @return - map of visited urls and their content
     */

    public Map<String, String> crawl(List<String> startUrls, int maxDepth, int maxUrlsPerPage){
        Queue<String> crawlQueue = new LinkedList<>(startUrls);
        Set<String> visitedUrls = new HashSet<>();
        Map<String, String> ret = new HashMap<>();
        int depth = 0;

        while(!crawlQueue.isEmpty() && depth <= maxDepth){
            int size = crawlQueue.size();

            for(int i = 0; i < size; i++){
                String url = crawlQueue.poll();
                if(visitedUrls.contains(url)) continue;
                visitedUrls.add(url);

                ScrapeRecord record = webScrapingService.scrape(url);

                if(!record.content().isEmpty()){
                    ret.put(url, record.content());

                    int currAdded = 0;
                    for(String link: record.links()){
                        if(visitedUrls.contains(link)) continue;
                        if(currAdded >= maxUrlsPerPage) break;

                        crawlQueue.add(link);
                        currAdded++;
                    }
                }
            }

            depth++;
        }

        return ret;
    }
}
