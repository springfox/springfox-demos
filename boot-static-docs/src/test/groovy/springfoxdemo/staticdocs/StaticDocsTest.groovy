package springfoxdemo.staticdocs

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import springfox.documentation.staticdocs.Swagger2MarkupResultHandler

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("swagger")
@ContextConfiguration(
        loader = SpringApplicationContextLoader, classes = Application)
@WebAppConfiguration
@TestExecutionListeners([DependencyInjectionTestExecutionListener, DirtiesContextTestExecutionListener])
class StaticDocsTest extends spock.lang.Specification {


  @Autowired
  WebApplicationContext context;

  MockMvc mockMvc;

  def setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build()
//            .apply(new RestDocumentationConfigurer()).build();
  }


  def "generates the petstore aip asciidoc"() {
    expect:
      Swagger2MarkupResultHandler resultHandler = Swagger2MarkupResultHandler.convertIntoFolder("swagger_adoc").build()

      this.mockMvc.perform(get("/v2/api-docs").accept(MediaType.APPLICATION_JSON))
              .andDo(resultHandler)
              .andExpect(status().isOk())
  }
}
