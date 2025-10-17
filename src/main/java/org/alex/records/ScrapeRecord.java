package org.alex.records;

import java.util.List;

// Utility record for holding scraped data
public record ScrapeRecord(String url, String content, List<String> links) {
}
