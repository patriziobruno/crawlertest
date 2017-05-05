package net.dstc.crawlertest.service;

import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.dstc.crawlertest.net.HttpCrawler;
import net.dstc.crawlertest.net.HttpCrawlerImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of a crawling service for optimizable sites
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class CrawlerServiceImpl implements CrawlerService {

    /**
     * Running crawling operations
     */
    private final ConcurrentMap<URL, CrawlingStatus> running;
    private final ExecutorService runner;

    @Autowired
    private HttpCrawler crawler;

    public CrawlerServiceImpl() {
        running = new ConcurrentHashMap();
        runner = Executors.newSingleThreadExecutor();
    }

    /**
     * Queues a new crawling request.
     *
     * @param url location of a page to be crawled
     * @param requestId request unique ID
     * @param handler function called at each status change (RUNNING, ERROR,
     * DONE)
     */
    @Override
    public void enqueue(URL url, UUID requestId,
            CrawlingCompletedFunction handler) {
        
        runner.execute(() -> {

            CrawlingStatus status = new CrawlingStatus();

            status.setCode(CrawlingStatus.StatusCode.RUNNING);
            status.setUrl(url.toString());

            running.put(url, status);

            // notifies the caller that we're about to start crawling (status = RUNNING)
            // the caller will receive a requestID that can be used by subsequent
            // REST call to retrieve crawling status
            handler.handle(status);
            try {
                status.setOptimizable(checkIfOptimizable(url, requestId));
                status.setCode(CrawlingStatus.StatusCode.DONE);
            } catch (Exception ex) {
                status.setError(ex);
            }
            running.remove(url);
            handler.handle(status);
        });
    }

    /**
     * Calls the HTTP crawler ({@link HttpCrawlerImpl) to crawl the page pointed by {@code url}.
     *
     * @param url URL pointing to the page to be crawled
     * @param requestId request unique ID
     * @return true if at least one optimizable page has been found
     * @throws Exception whatever happens will be reported to the client
     */
    private boolean checkIfOptimizable(URL url, UUID requestId) throws
            Exception {
        List<URL> optimizablePages = crawler.run(url, requestId);
        return optimizablePages.size() > 0;
    }

    public ConcurrentMap<URL, CrawlingStatus> running() {
        return running;
    }
}
