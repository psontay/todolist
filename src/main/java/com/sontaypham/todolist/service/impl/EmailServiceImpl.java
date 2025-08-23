package com.sontaypham.todolist.service.impl;

import com.sontaypham.todolist.dto.response.EmailResponse;
import com.sontaypham.todolist.entities.EmailDetails;
import com.sontaypham.todolist.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailServiceImpl implements EmailService {
  @Value("${spring.mail.username}")
  String host;
  String cachedCss;
  final JavaMailSender mailSender;
  final TemplateEngine templateEngine;
  @Override
  public EmailResponse sendTextEmail(EmailDetails details) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(host);
      message.setTo(details.getTo());
      message.setSubject(details.getSubject());
      message.setText(details.getMessageBody());
      mailSender.send(message);
      return EmailResponse.builder()
          .success(true)
          .message("Email sent successfully to " + details.getTo())
          .build();
    } catch (Exception e) {
      log.error(e.getMessage());
      return EmailResponse.builder()
          .success(false)
          .message("Failed to sent email : " + e.getMessage())
          .build();
    }
  }


    @Override
    public EmailResponse sendTemplateHtmlEmail(EmailDetails details) {
        try{
            ClassPathResource resource = new ClassPathResource("templates/css/default.css");
            String cssContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            Context context = new Context();
            if (details.getVariables() != null) {
                details.getVariables().forEach(context::setVariable);
            }
            context.setVariable("cssContent", cssContent);
            if (details.getVariables() != null) {
                details.getVariables().forEach((k, v) -> {
                    log.info("Set Thymeleaf variable: {} = {}", k, v);
                    context.setVariable(k, v);
                });
            }

            String htmlContent = templateEngine.process(details.getTemplateName(), context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(host);
            helper.setTo(details.getTo());
            helper.setSubject(details.getSubject());
            helper.setText(htmlContent, true);

            mailSender.send(message);

            return EmailResponse.builder()
                                .success(true)
                                .message("Template email sent successfully to " + details.getTo())
                                .build();
        }catch (Exception e){
            log.error("Failed to send template email", e);
            return EmailResponse.builder()
                                .success(false)
                                .message("Failed to send template email: " + e.getMessage())
                                .build();
        }
    }

    @Override
    public EmailResponse sendAttachmentEmail(EmailDetails details) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(host);
            helper.setTo(details.getTo());
            helper.setSubject(details.getSubject());
            helper.setText(details.getMessageBody());
            if ( details.getAttachmentPaths() != null) {
                for ( String path : details.getAttachmentPaths() ) {
                    FileSystemResource file = new FileSystemResource(new File(path));
                    helper.addAttachment(file.getFilename(), file);
                }
            }
            mailSender.send(message);
            return EmailResponse.builder()
                                .success(true)
                                .message("Email sent successfully to " + details.getTo())
                                .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return EmailResponse.builder()
                                .success(false)
                                .message("Failed to sent email : " + e.getMessage())
                                .build();
        }    }
    private String getCssContent() {
        if (cachedCss == null) {
            try {
                ClassPathResource resource = new ClassPathResource("templates/css/email.css");
                cachedCss = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("Failed to load CSS", e);
                cachedCss = "";
            }
        }
        return cachedCss;
    }

}
