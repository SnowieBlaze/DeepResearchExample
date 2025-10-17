# Deep Research Agent
This is a multi-agent AI system built in Java that performs deep research on a given topic, synthesizes multiple web sources, and generates a report in .txt format.

## Features
- LLM Query Generation: Uses an AI agent to transform a topic into multiple specific Google search queries.
- Web Crawling: Starts with the seed URLs generated from the search and crawls linked pages.
- Web Scraping: Uses Selenium in order to scrape JavaScript-heavy websites.
- Relevance Filtering: Uses a RelevancyAgent in order to discard crawled sources that are not relevant to the original topic.
- Map-Reduce Synthesis: The ResearchAgent uses a map-reduce approach to take content from all the pages, summarise it, then turn it into a report.

## Architecture / Workflow
The research agent workflow is as follows:
- First, the search agent creates a list of queries adequate for a given topic.
- Next, using the google search api, relevant urls are gathered.
- Next, the web crawler service crawls and scrapes those url up to a desired depth.
- The crawled results are then put into 2 buckets: relevant or irrelevant using the relevancy agent
- Finally, each of the relevant results is summarised, and then all of these summaries are used to assemble a report.

## Requirements:
- Java 21+
- Google Chrome (the web scraper uses chrome as a driver)

## Setup and Usage
- First, clone the repository.
  `git clone https://github.com/SnowieBlaze/DeepResearchExample`
- Next, create a .env file where you hold the following:
  ```
    OPENAI_API_KEY=your open ai key
    GOOGLE_SEARCH_API_KEY=your google search api key
    CX_ID=your programmable search engine id
  
  Below values can be customized, defaults are used here
    CACHE_FILE_PATH=search_results.txt 
    RESEARCH_TOPIC="How to build a deep research agent"
    LLM_QUERY_AMOUNT="3"
    RESULTS_PER_QUERY="3"
    CRAWL_DEPTH="2"
    MAX_CRAWL_URLS_PER_PAGE="3"
  ```
- Build the project using gradle.
  ```
  Windows: .\gradlew build
  Linux: ./gradlew build
  ```
- Finally, run the project, either from your IDE using Main.java, or using Gradle.
  ```
  Windows: .\gradlew run
  Linux: ./gradlew run
  ```
  

## Tips
- New Research Topics: To research a new topic, simply delete the search_results.txt file from the project root and update the environment variable. This will trigger a new live search.
- Manual URL Input: You can bypass the Google Search step entirely by manually pasting your own list of URLs (one per line) into the cache file(default: search_results.txt). The agent will use these on its next run.
- Output Location: The final report will be saved as a .txt project root, named according to the research topic (e.g., Report-How_to_build_a_deep_research_agent.txt).
- Sample Report: A sample generated report for the topic "How to build a deep research agent" is included in the project root for reference
- Note: Running a research topic will take around 5 minutes on default settings.