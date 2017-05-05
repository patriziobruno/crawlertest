package net.dstc.crawlertest.service;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class CrawlingStatusTest {

    @Test
    public void testGetSetUrl() {
        CrawlingStatus test = new CrawlingStatus();
        String url = "http://test.test";
        test.setUrl(url);
        assertEquals(url, test.getUrl());
    }

    @Test
    public void testSetGetIsError() {
        CrawlingStatus test = new CrawlingStatus();
        Exception exception = new Exception("test");
        test.setError(exception);
        assertEquals(exception.toString(), test.getError());
        assertEquals(CrawlingStatus.StatusCode.ERROR, test.getCode());
    }

    @Test
    public void testGetSetCode() {
        CrawlingStatus test = new CrawlingStatus();
        test.setCode(CrawlingStatus.StatusCode.DONE);
        assertEquals(CrawlingStatus.StatusCode.DONE, test.getCode());
    }

    @Test
    public void testIsGetOptimizable() {
        CrawlingStatus test = new CrawlingStatus();
        test.setOptimizable(true);
        assertTrue(test.isOptimizable());
    }

    @Test
    public void testGetSetId() {
        CrawlingStatus test = new CrawlingStatus();
        test.setId("test");
        assertEquals("test", test.getId());
    }
}
