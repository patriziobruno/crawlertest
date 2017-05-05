package net.dstc.crawlertest.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import mockit.Injectable;
import mockit.Tested;
import net.dstc.crawlertest.net.HttpCrawler;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class CrawlerServiceImplTest {

    @Tested
    private CrawlerServiceImpl service;

    @Injectable
    private HttpCrawler crawler;

    @Test
    public void enqueue() throws MalformedURLException, InterruptedException {
        String testUrl = "http://test.com";
        UUID id = UUID.randomUUID();
        final CrawlingStatus rv = new CrawlingStatus();
        
        service.enqueue(new URL(testUrl), id, (status) -> {
            rv.setCode(status.getCode());
            rv.setId(status.getId());
            rv.setUrl(status.getUrl());
        });
        
        Thread.sleep(50);
        
        assertEquals(CrawlingStatus.StatusCode.DONE, rv.getCode());
        assertEquals(testUrl, rv.getUrl());
    }
}
