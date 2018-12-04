package com.escalon.springfox.springintegration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springfox.documentation.spring.web.SpringfoxTemplateFormat;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringIntegrationWebMvcApplication.class)
public class SpringIntegrationWebMvcApplicationTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();


  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
      .apply(documentationConfiguration(this.restDocumentation)
          .snippets()
          .withTemplateFormat(new SpringfoxTemplateFormat())

//            .and()
//            .snippets()
//            .withTemplateFormat(TemplateFormats.asciidoctor())
      )
      .build();
  }

  @Test
  public void toLowerFlowAragorn() throws Exception {
    mockMvc.perform(
      MockMvcRequestBuilders.post("/conversions/lower")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" +
          "  \"bar\": \"Aragorn\",\n" +
          "  \"foo\": true,\n" +
          "  \"count\": 3\n" +
          "}"))
      .andExpect(MockMvcResultMatchers.status()
        .isOk())
      .andDo(document("toLowerGatewayAragorn", responseFields(
        fieldWithPath("bar").description("Name of the bar"),
        fieldWithPath("foo").description("Specifies if this is a foo"),
        fieldWithPath("count").description("Specifies how many foos there are")
      )));

  }

  @Test
  public void toLowerFlowGimli() throws Exception {
    mockMvc.perform(
      MockMvcRequestBuilders.post("/conversions/lower")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" +
          "  \"bar\": \"Gimli\",\n" +
          "  \"foo\": true,\n" +
          "  \"count\": 3\n" +
          "}"))
      .andExpect(MockMvcResultMatchers.status()
        .isOk())
      .andDo(document("toLowerGatewayGimli", responseFields(
        fieldWithPath("bar").description("Name of the bar"),
        fieldWithPath("foo").description("Specifies if this is a foo"),
        fieldWithPath("count").description("Specifies how many foos there are")
      )));

  }


}