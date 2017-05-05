package net.dstc.crawlertest.rest;

import java.net.URL;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * This class holds the status of a running crawling request
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class RunningRequest {

    /**
     * URLs contained in a crawling request received by a client
     */
    final Collection<URL> urls;
    /**
     * Server Socket Event Emitter, clients requesting it get real-time
     * notifications about crawling operation states.
     */
    final SseEmitter emitter;

    public RunningRequest(SseEmitter emitter) {
        this.urls = new ConcurrentLinkedDeque<>();
        this.emitter = emitter;
    }
}
