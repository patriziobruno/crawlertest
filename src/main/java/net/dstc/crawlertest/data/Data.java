package net.dstc.crawlertest.data;

import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Interface implemented by classes holding data access logic
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public interface Data {

    /**
     * Save an optimizable URL to the underlying data storage
     *
     * @param url optimizable URL
     * @param title title of the page pointed by the URL
     * @param downloadTime page download and parse time in milliseconds
     * @param uuid request identifier
     */
    void save(URL url, String title, long downloadTime, UUID uuid);

    /**
     * Find all the crawled optimizable URLs
     * 
     * @return a list of optimizable URLs and related information
     */
    List<Optimizable> list(String uuid);
}
