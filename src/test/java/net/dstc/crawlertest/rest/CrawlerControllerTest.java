package net.dstc.crawlertest.rest;

import java.net.URL;
import java.util.UUID;
import mockit.Injectable;
import mockit.Tested;
import net.dstc.crawlertest.rest.model.CrawlingRequest;
import net.dstc.crawlertest.rest.model.CrawlingRequests;
import net.dstc.crawlertest.service.CrawlerService;
import net.dstc.crawlertest.service.CrawlingStatus;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mockit.Expectations;
import net.dstc.crawlertest.rest.model.CrawlingRequestStatus;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")
@WebAppConfiguration
public final class CrawlerControllerTest {

    static final String TEST_ID = UUID.randomUUID().toString();
    URL testUrl;

    @Tested(availableDuringSetup = true)
    CrawlerController controller;
    @Injectable
    RunningRequests runningRequests;
    @Injectable
    CrawlerService service;

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        testUrl = new URL("http://test.com");
    }

    @Test
    public void startReturnsUUID() throws Exception {
        CrawlingRequests requests = new CrawlingRequests();
        CrawlingRequest request = new CrawlingRequest();
        request.setUrl(testUrl.toString());
        requests.add(request);
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/")
                .content(mapper.writeValueAsString(requests))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new BaseMatcher<String>() {
                    @Override
                    public boolean matches(Object item) {
                        try {
                            String result = mapper.readValue(item.toString(), String.class);
                            UUID.fromString(result);
                        } catch (IllegalArgumentException | IOException ex) {
                            Logger.getLogger(CrawlerControllerTest.class.getName()).log(Level.SEVERE, item.toString(), ex);
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                }));
    }

    @Test
    public void listenReturnsSSE() throws Exception {
        provideARunningRequestGivenTestId();
        String path = "/" + TEST_ID;

        mockMvc.perform(get(path)
                .pathInfo(path)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .header("Connection", "keep-alive")
                .header("Cache-Control", "no-cache")
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted());
    }

    void provideARunningRequestGivenTestId() {
        RunningRequest runningRequest = new RunningRequest(new SseEmitter());
        runningRequest.urls.add(testUrl);

        new Expectations() {
            {
                runningRequests.isRunning(TEST_ID);
                result = true;
                runningRequests.get(TEST_ID);
                result = runningRequest;
            }
        };
    }

    @Test
    public void whenListenOnNonExistingIdExpect404() throws Exception {
        mockMvc.perform(get("/doesnt-exist-" + TEST_ID)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .header("Connection", "keep-alive")
                .header("Cache-Control", "no-cache")
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void getStatusReturnsExpectedJsonObject() throws Exception {
        CrawlingStatus status = new CrawlingStatus();
        status.setCode(CrawlingStatus.StatusCode.RUNNING);
        status.setId(TEST_ID);
        status.setUrl(testUrl.toString());
        ConcurrentMap<URL, CrawlingStatus> runningOperations = new ConcurrentHashMap<>();
        runningOperations.put(testUrl, status);

        new Expectations() {
            {
                service.running();
                result = runningOperations;
            }
        };
        provideARunningRequestGivenTestId();

        CrawlingRequestStatus expected = new CrawlingRequestStatus();
        expected.put(testUrl, new CrawlingStatus() {
            {
                setUrl(testUrl.toString());
                setCode(StatusCode.RUNNING);
                setId(TEST_ID);
            }
        });
        String path = "/status/" + TEST_ID;

        mockMvc.perform(get(path)
                .pathInfo(path)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expected)));
    }

    @Test
    public void whenGetStatusOnNonExistingIdExpect404() throws Exception {
        String path = "/status/doesnt-exist-" + TEST_ID;

        mockMvc.perform(get(path)
                .pathInfo(path)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }
}
