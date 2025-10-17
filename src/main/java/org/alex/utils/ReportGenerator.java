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
        try {
            String filename = "Report-" + config.researchTopic().replaceAll("[^a-zA-Z0-9]", "_");
            String extension = ".txt";

            Path finalFilePath = Paths.get(filename + extension);
            int counter = 1;
            while (Files.exists(finalFilePath)) {
                String newFileName = filename + "_" + counter + extension;
                finalFilePath = Paths.get(newFileName);
                counter++;
            }

            String fileHeader = "Research topic: " + config.researchTopic() + " \n";
            String fullContentToWrite = fileHeader + reportContent;

            Files.writeString(finalFilePath, fullContentToWrite, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.err.println("Error when saving report: " + e.getMessage());
        }
    }
}