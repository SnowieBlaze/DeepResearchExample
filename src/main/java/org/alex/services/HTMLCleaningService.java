package org.alex.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HTMLCleaningService {
    // This should ideally just come from a config file
    private static final String IGNORE = "script, style, noscript, iframe, object, embed, svg, " +
            "nav, footer, header, aside, form, " +
            ".ad, .ads, .advert, .advertisement, [class*='ad-'], [id*='ad-'], " +
            ".sidebar, .comment, .comments, .hidden, [style*='display:none']";

    public HTMLCleaningService(){}

    /**
     * Cleans a raw html string by removing unwanted elements and attributes.
     * @param rawHtml - raw html string to clean
     * @return - cleaned html document
     */
    public Document clean(String rawHtml) {
        if (rawHtml == null || rawHtml.isEmpty()) {
            return Jsoup.parse("");
        }

        Document ret = Jsoup.parse(rawHtml);
        ret.select(IGNORE).remove();

        return ret;
    }

}
