package net.dstc.crawlertest.rest.model;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import net.dstc.crawlertest.service.CrawlingStatus;

/**
 * Status of a crawling operation. Keys are the URL sent by a request.
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class CrawlingRequestStatus extends HashMap<URL, CrawlingStatus> {

    public CrawlingRequestStatus() {
    }

    public CrawlingRequestStatus(Map<URL, CrawlingStatus> map) {
        super(map);
    }
}
