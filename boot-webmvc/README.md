# boot-swagger
A spring boot application with OpenAPI 3.0 api documentation enabled
 - Demonstrates using the default swagger group 
 - Demonstrates using context path of `/mvc`
 - Demonstrates use of path for swagger-ui @ `/documentation`
```
./gradlew boot-webflux:bootRun
```

## Paths
- All Swagger Resources(groups) `http://localhost:8080/mvc/documentation/swagger-resources`
- Swagger UI endpoint: `http://localhost:8080/mvc/documentation/swagger-ui/`
- Swagger docs endpoint: `http://localhost:8080/mvc/v3/api-docs`


