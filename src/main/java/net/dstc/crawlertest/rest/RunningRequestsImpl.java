package net.dstc.crawlertest.rest;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of a List of running crawling requests
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class RunningRequestsImpl extends ConcurrentHashMap<String, RunningRequest> implements RunningRequests {

    @Override
    public boolean isRunning(String id) {
        return containsKey(id);
    }

    @Override
    public RunningRequest get(String id) {
        return super.get(id);
    }

}
