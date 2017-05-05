package net.dstc.crawlertest.service;

import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * This interface defines a service devoted to crawl info about optimizable
 * sites.
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public interface CrawlerService {

    /**
     * Queues a new crawling request.
     * @param url location of a page to be crawled
     * @param requestId request unique ID
     * @param handler function called at each status change (RUNNING, ERROR, DONE)
     */
    void enqueue(URL url, UUID requestId, CrawlingCompletedFunction handler);

    /**
     * Lists running crawling operations
     * @return a List of running crawling operations
     */
    ConcurrentMap<URL, CrawlingStatus> running();
}
