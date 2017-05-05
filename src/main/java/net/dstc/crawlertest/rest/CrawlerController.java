package net.dstc.crawlertest.rest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import net.dstc.crawlertest.rest.model.CrawlingRequestStatus;
import net.dstc.crawlertest.rest.model.CrawlingRequests;
import net.dstc.crawlertest.service.CrawlerService;
import net.dstc.crawlertest.service.CrawlingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * This controller exposes REST endpoints to handle crawling requests
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
@RestController("/crawler")
public class CrawlerController {

    @Autowired
    private CrawlerService service;

    @Autowired
    private RunningRequests runningRequests;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UUID start(@RequestBody(required = false) CrawlingRequests requests) {

        UUID requestId = UUID.randomUUID();
        String reqIdStr = requestId.toString();
        SseEmitter emitter = new SseEmitter();
        AtomicInteger count = new AtomicInteger(requests.size());
        RunningRequest rr = new RunningRequest(emitter);

        requests.urls().forEach(purl -> {
            if (purl.isOK()) {
                service.enqueue(purl.getUrl(), requestId, (status) -> {
                    try {
                        status.setId(reqIdStr);
                        emitter.send(status, MediaType.APPLICATION_JSON);
                    } catch (IOException ex) {
                        Logger.getLogger(CrawlerController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (status.getCode() != CrawlingStatus.StatusCode.RUNNING && count.decrementAndGet() == 0) {
                        emitter.complete();
                        runningRequests.remove(reqIdStr);
                    }
                });
                rr.urls.add(purl.getUrl());
                if (!runningRequests.containsKey(reqIdStr)) {
                    runningRequests.put(reqIdStr, rr);
                }
            } else {
                CrawlingStatus status = new CrawlingStatus();

                status.setId(reqIdStr);
                status.setCode(CrawlingStatus.StatusCode.ERROR);
                status.setUrl(purl.getUnparsed());
                status.setError(purl.getError());

                try {
                    emitter.send(status, MediaType.APPLICATION_JSON);
                } catch (IOException ex) {
                    Logger.getLogger(CrawlerController.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (count.decrementAndGet() == 0) {
                    emitter.complete();
                }
            }
        });

        return requestId;
    }

    @GetMapping("/{id}")
    public SseEmitter listen(HttpServletResponse response, @PathVariable String id) {

        if (runningRequests.isRunning(id)) {
            RunningRequest rr = runningRequests.get(id);
            if (rr != null) {
                return rr.emitter;
            }
        }
        response.setStatus(404);
        return null;
    }

    @GetMapping("/status/{id}")
    public CrawlingRequestStatus getStatus(HttpServletResponse response, @PathVariable String id) {
        final CrawlingRequestStatus rv;
        if (runningRequests.isRunning(id)) {
            rv = new CrawlingRequestStatus(
                    runningRequests.get(id).urls
                            .stream()
                            .collect(Collectors.toMap(url -> url, url -> service.running().get(url)))
            );
        } else {
            response.setStatus(404);
            rv = null;
        }
        return rv;
    }

    @GetMapping("/status")
    public List<CrawlingRequestStatus> getStatusAll(HttpServletResponse response) {
        final List<CrawlingRequestStatus> rv
                = runningRequests.entrySet().stream().map(entry -> {
                    CrawlingRequestStatus status = new CrawlingRequestStatus(
                            entry.getValue().urls
                                    .stream()
                                    .collect(Collectors.toMap(url -> url, url -> service.running().get(url)))
                    );
                    return status;
                }).collect(Collectors.toList());
        return rv;
    }
}
