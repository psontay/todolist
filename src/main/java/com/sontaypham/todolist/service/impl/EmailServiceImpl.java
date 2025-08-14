package com.sontaypham.todolist.service.impl;

import com.sontaypham.todolist.dto.response.EmailResponse;
import com.sontaypham.todolist.entities.EmailDetails;
import com.sontaypham.todolist.repository.EmailRepository;
import com.sontaypham.todolist.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
  @Value("${spring.mail.username}")
  String from;
  private final JavaMailSender mailSender;

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
