package net.dstc.crawlertest.rest.model;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * A crawling request sent by the client, containing an entry per-each URL to be
 * crawled.
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class CrawlingRequests extends ArrayList<CrawlingRequest> {

    /**
     * All urls contained in this list of requests sent by the client.
     *
     * @return
     */
    public Stream<ParsedURL> urls() {
        return stream().map(req -> new ParsedURL(req.getUrl()));
    }
}
