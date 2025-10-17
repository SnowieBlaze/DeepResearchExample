package org.alex.services;

import org.alex.records.ScrapeRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebScrapingService {

    private final WebDriver webDriver;

    public WebScrapingService() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        this.webDriver = new ChromeDriver(options);
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
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(2L));
            WebElement bodyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

            List<WebElement> linkElems = webDriver.findElements(By.tagName("a"));
            List<String> links = linkElems.stream()
                    .map(e -> e.getAttribute("href"))
                    .filter(e -> e != null && (e.startsWith("http") || e.startsWith("https")))
                    .distinct()
                    .toList();

            return new ScrapeRecord(url, bodyElement.getText(), links);

        } catch (Exception e) {
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
