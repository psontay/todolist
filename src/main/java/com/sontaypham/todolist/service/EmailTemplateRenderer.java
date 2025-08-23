package com.sontaypham.todolist.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailTemplateRenderer {
    private final TemplateEngine templateEngine;
    private String cssContent;
    public EmailTemplateRenderer(TemplateEngine templateEngine) throws IOException {
        this.templateEngine = templateEngine;
        ClassPathResource cssResource = new ClassPathResource("templates/css/default.css");
        this.cssContent = new String(cssResource.getInputStream().readAllBytes() , StandardCharsets.UTF_8);
    }

}
