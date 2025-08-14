package com.sontaypham.todolist.service;

import com.sontaypham.todolist.dto.response.EmailResponse;
import com.sontaypham.todolist.entities.EmailDetails;

public interface EmailService {
  EmailResponse sendSimpleMail(EmailDetails details);

  void sendMailWithAttachment(EmailDetails details);
}
