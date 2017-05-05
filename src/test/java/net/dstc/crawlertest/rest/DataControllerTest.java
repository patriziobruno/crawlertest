package net.dstc.crawlertest.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.UUID;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import net.dstc.crawlertest.data.Data;
import net.dstc.crawlertest.data.Optimizable;
import org.junit.Test;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public final class DataControllerTest {

    final UUID testId = UUID.randomUUID();
    final List<Optimizable> expected = asList(new Optimizable("http://test.com", "news", 100, testId.toString()));

    @Tested(availableDuringSetup = true, fullyInitialized = true)
    DataController controller;
    @Injectable
    Data data;

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void list() throws Exception {
        new Expectations() {
            {
                data.list(testId.toString());
                result = expected;
            }
        };
        String path = "/list/" + testId;

        mockMvc.perform(get(path)
                .pathInfo(path)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expected)));
    }

    @Test
    public void whenListOnNonExistingIdExpectEmptyList() throws Exception {
        String path = "/list/doesnt-exist-" + testId;

        mockMvc.perform(get(path)
                .pathInfo(path)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    public void listAll() throws Exception {
        new Expectations() {
            {
                data.list(null);
                result = expected;
            }
        };

        mockMvc.perform(get("/list")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expected)));
    }
}
