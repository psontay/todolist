package com.sontaypham.todolist.repository;

import com.sontaypham.todolist.dto.response.EmailResponse;
import com.sontaypham.todolist.entities.EmailDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository {
  EmailResponse sendSimpleMail(EmailDetails details);

  void sendMailWithAttachment(EmailDetails details);
}
