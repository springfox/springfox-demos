## boot-static-docs

__Experimental__
- A spring boot app with a default swagger2 configuration
- A test which uses `springfox-staticdocs` and [swagger2markup](https://github.com/RobWin/swagger2markup) to generate 
 Asciidoc source from the applications JSO API.
- The [asciidoctor-gradle-plugin](https://github.com/asciidoctor/asciidoctor-gradle-plugin) to generate pdf and html asciidoctor docs.
 
 
To generate the asciidoctor documentation run:
 
```bash
./gradlew boot-static-docs:asciidoctor
```

### Runnng the app
```bash
./gradlew boot-static-docs:bootRun 
```

http://localhost:8080/v2/api-docs
http://localhost:8080/swagger-ui.html
