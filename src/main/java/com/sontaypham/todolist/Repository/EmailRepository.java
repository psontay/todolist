package com.sontaypham.todolist.Repository;

import com.sontaypham.todolist.Entities.EmailDetails;

public interface EmailRepository {
        void sendSimpleMail(EmailDetails details);

        void sendMailWithAttachment(EmailDetails details);
}
