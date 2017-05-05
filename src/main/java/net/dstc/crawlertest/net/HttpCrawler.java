package net.dstc.crawlertest.net;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * This interface defines a HttpCrawler used to crawl sites to check if they're
 * optimizable
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public interface HttpCrawler {

    /**
     * Runs a crawling operation over a URL
     *
     * @param url URL to crawl
     * @param requestId request unique ID
     * @return a list of URLs connected to the crawled URL
     * @throws IOException error retrieving/parsing the first URL passed in as
     * parameter
     */
    public List<URL> run(URL url, UUID requestId) throws IOException;
}
