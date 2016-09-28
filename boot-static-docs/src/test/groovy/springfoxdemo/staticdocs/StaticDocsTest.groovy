package springfoxdemo.staticdocs

import groovy.io.FileType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import springfox.documentation.staticdocs.Swagger2MarkupResultHandler

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ContextConfiguration(
        loader = SpringApplicationContextLoader,
        classes = Application)
@WebAppConfiguration
@TestExecutionListeners([DependencyInjectionTestExecutionListener, DirtiesContextTestExecutionListener])
class StaticDocsTest extends spock.lang.Specification {

  @Autowired
  WebApplicationContext context;

  MockMvc mockMvc;

  def setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build()
  }


  def "generates the petstore api asciidoc"() {
    setup:
      String outDir = System.getProperty('asciiDocOutputDir', 'build/aciidoc')
      Swagger2MarkupResultHandler resultHandler = Swagger2MarkupResultHandler
              .outputDirectory(outDir)
              .build()

    when:
      this.mockMvc.perform(get("/v2/api-docs").accept(MediaType.APPLICATION_JSON))
              .andDo(resultHandler)
              .andExpect(status().isOk())
    then:
      def list = []
      def dir = new File(resultHandler.outputDir)
      dir.eachFileRecurse(FileType.FILES) { file ->
        list << file.name
      }
      list.sort() == ['definitions.adoc', 'overview.adoc', 'paths.adoc']
  }
}
