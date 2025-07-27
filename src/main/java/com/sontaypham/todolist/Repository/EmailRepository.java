package com.sontaypham.todolist.Repository;

import com.sontaypham.todolist.DTO.Response.EmailResponse;
import com.sontaypham.todolist.Entities.EmailDetails;

public interface EmailRepository {
        EmailResponse sendSimpleMail(EmailDetails details);

        void sendMailWithAttachment(EmailDetails details);
}
