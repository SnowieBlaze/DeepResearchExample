package org.alex.utils;

import org.alex.config.AppConfig;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReportGenerator {

    /**
     * Saves the report content as a text file.
     * @param reportContent - report content to save
     * @param config - application config
     */
    public void saveAsTextFile(String reportContent, AppConfig config) {
        String fileName = "Report-" + config.researchTopic().replaceAll("[^a-zA-Z0-9]", "_") + ".txt";

        try {
            Path filePath = Paths.get(fileName);
            Files.writeString(filePath, reportContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}