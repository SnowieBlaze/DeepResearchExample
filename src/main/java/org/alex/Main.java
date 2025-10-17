package org.alex;

import org.alex.config.AppConfig;
import org.alex.services.LLMService;
import org.alex.utils.ReportGenerator;
import org.alex.utils.WebDataCollector;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        AppConfig config = AppConfig.load();

        LLMService llmService = new LLMService(config.openaiApiKey());
        WebDataCollector dataCollector = new WebDataCollector(llmService, config);

        Map<String, String> crawledData = dataCollector.collectData();

        if (crawledData.isEmpty()) {
            System.out.println("No crawled data.");
            return;
        }

        List<String> relevantContents = llmService.filterRelevantContent(config.researchTopic(), crawledData);
        if (relevantContents.isEmpty()) {
            System.out.println("No relevant content.");
            return;
        }

        String finalReport = llmService.generateReport(config.researchTopic(), relevantContents);

        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.saveAsTextFile(finalReport, config);
    }
}