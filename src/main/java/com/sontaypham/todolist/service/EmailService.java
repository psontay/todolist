package com.sontaypham.todolist.service;

import com.sontaypham.todolist.dto.response.EmailResponse;
import com.sontaypham.todolist.entities.EmailDetails;
import com.sontaypham.todolist.repository.EmailRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailService implements EmailRepository {
  @Value("${spring.mail.username}")
  String from;

  @Autowired JavaMailSender mailSender;

  @Override
  public EmailResponse sendSimpleMail(EmailDetails details) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(from);
      message.setTo(details.getTo());
      message.setSubject(details.getSubject());
      message.setText(details.getMessageBody());
      // sending email
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
  public void sendMailWithAttachment(EmailDetails details) {}
}
