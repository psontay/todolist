package com.sontaypham.todolist.Service;

import com.sontaypham.todolist.Entities.EmailDetails;
import com.sontaypham.todolist.Repository.EmailRepository;

public class EmailService implements EmailRepository {

    @Override
    public void sendSimpleMail(EmailDetails details) {

    }

    @Override
    public void sendMailWithAttachment(EmailDetails details) {
        return "";
    }
}
