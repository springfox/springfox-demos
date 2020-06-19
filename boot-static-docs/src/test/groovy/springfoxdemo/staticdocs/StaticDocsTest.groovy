package springfoxdemo.staticdocs

import groovy.io.FileType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Ignore
import spock.lang.Specification
import springfox.documentation.staticdocs.Swagger2MarkupResultHandler

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@WebMvcTest
class StaticDocsTest extends Specification {

  @Autowired
  MockMvc mockMvc;

  @Ignore
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
