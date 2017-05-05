package net.dstc.crawlertest.service;

/**
 * This function is called at every crawling status change.
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
@FunctionalInterface
public interface CrawlingCompletedFunction {

    void handle(CrawlingStatus result);
}
