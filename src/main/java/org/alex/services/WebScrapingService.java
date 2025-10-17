package org.alex.services;

import org.alex.records.ScrapeRecord;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WebScrapingService {

    private final WebDriver webDriver;
    private final HTMLCleaningService htmlCleaningService;

    public WebScrapingService(HTMLCleaningService htmlCleaningService) {

        ChromeOptions options = new ChromeOptions();
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36";
        options.addArguments("--user-agent=" + userAgent);

        // Needed to not be detected as bot immediately
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        this.webDriver = new ChromeDriver(options);
        this.htmlCleaningService = htmlCleaningService;
    }

    /**
     * Scrapes a website and returns a record with the following fields:
     * The url of the website, the content(the entire body) and the list of links found on the website.
     * @param url - url of the website to scrape
     * @return - record with the scraped website's content and links
     */

    public ScrapeRecord scrape(String url){
        try {
            webDriver.get(url);
            new FluentWait<>(webDriver)
                    .withTimeout(Duration.ofSeconds(5))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(Exception.class)
                    .until(webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState").equals("complete"));

            Document cleanedDoc = htmlCleaningService.clean(webDriver.getPageSource());
            String content = cleanedDoc.text();

            List<String> links = cleanedDoc.select("a").stream()
                    .map(e -> e.attr("abs:href"))
                    .filter(e -> !e.isEmpty())
                    .distinct()
                    .toList();

            return new ScrapeRecord(url, content, links);

        } catch (Exception e) {
            System.out.println("--> SCRAPE FAILED for URL: " + url + " | Reason: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return new ScrapeRecord(url, "", new ArrayList<>());
        }
    }

    /**
     * Method to close the web driver.
     */
    public void close(){
        if(webDriver != null){
            webDriver.quit();
        }
    }
}
