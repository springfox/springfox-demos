package com.escalon.springfox.springintegration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringIntegrationWebMvcApplication.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class SpringIntegrationApplicationIT {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();


    @Test
    public void toLowerFlow() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/conversions/lower")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"bar\": \"Aragorn\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andDo(document("lower"));

    }


}