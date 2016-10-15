## boot-static-docs

__Experimental__

### Contents

- A spring boot [app](src/main/java/springfoxdemo/staticdocs/Application.java) with a default swagger2 configuration

- A [test](src/test/groovy/springfoxdemo/staticdocs/JsonDefinitionTest.groovy) which uses `springfox-staticdocs` to output JSON Swagger definition for the application.

- A [test](src/test/groovy/springfoxdemo/staticdocs/AsciiDocTest.groovy) which uses `springfox-staticdocs` and [swagger2markup](https://github.com/RobWin/swagger2markup) to generate 
 Asciidoc source (HTML, PDF) from the applications JSON Swagger definition.

- The [asciidoctor-gradle-plugin](https://github.com/asciidoctor/asciidoctor-gradle-plugin) to generate pdf and html asciidoctor docs.
 
### Running the demo
 
To generate the documentation run:
 
```bash
./gradlew boot-static-docs:test
```

Generated documentation artifacts will be found in `build/jsondef` and `build/asciidoc`.

### Running the app
```bash
./gradlew boot-static-docs:bootRun 
```

http://localhost:8080/v2/api-docs

http://localhost:8080/swagger-ui.html
