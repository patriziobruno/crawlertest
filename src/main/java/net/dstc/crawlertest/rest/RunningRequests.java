package net.dstc.crawlertest.rest;

import java.util.concurrent.ConcurrentMap;

/**
 * Interface of a list of running crawling requests
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public interface RunningRequests extends ConcurrentMap<String, RunningRequest> {

    /**
     * Verify if a given request is still running
     *
     * @param id request id
     * @return true if the request is still running
     */
    boolean isRunning(String id);

    /**
     * Retrieves a running request identified by {@code id}
     * @param id id of the request to retrieve
     * @return a running request, if it exists, {@code null} otherwise
     */
    RunningRequest get(String id);
}
