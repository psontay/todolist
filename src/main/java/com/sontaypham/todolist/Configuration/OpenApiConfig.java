package com.sontaypham.todolist.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
            .info(new Info()
                          .title("Todolist API Documentation")
                          .description("""
                This is the official API documentation for the Todolist application.
                It provides endpoints to manage users, roles, tasks, and permissions.
                Built with Spring Boot and follows RESTful best practices.
                """)
                          .version("v1.0.0")
                          .contact(new Contact()
                                           .name("Son Tay")
                                           .email("phamsontay2206@gmail.com")
                                           .url("https://github.com/sontay226/todolist"))
                          .license(new License()
                                           .name("Apache 2.0")
                                           .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
            .servers(List.of( new Server().url("http://localhost:8080/")));
  }
}
