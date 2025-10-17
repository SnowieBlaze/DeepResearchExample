package org.alex.services;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.customsearch.v1.CustomSearchAPI;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WebSearchService {

    private static final String API_KEY = System.getenv("GOOGLE_SEARCH_API_KEY");
    private static final String CX_ID = System.getenv("CX_ID");
    private final CustomSearchAPI customsearch;

    public WebSearchService() {
        this.customsearch = new CustomSearchAPI.Builder(new NetHttpTransport(), new GsonFactory(), null).setApplicationName("Deep research").build();
    }

    /**
     * Search for a query using google custom search api
     * @param query - query to search for
     * @param limit - maximum number of results to return
     * @return - list of result urls
     */

    public List<String> search(String query, Integer limit){
        try {
            CustomSearchAPI.Cse.List request = customsearch.cse().list();

            request.setKey(API_KEY);
            request.setCx(CX_ID);
            request.setQ(query);
            request.setNum(limit);

            Search result = request.execute();
            List<Result> items = result.getItems();
            List<String> urls = new ArrayList<>();

            for(Result item: items){
                urls.add(item.getLink());
            }
            return urls;
        } catch (IOException e) {
            System.out.println("Error during google search: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
