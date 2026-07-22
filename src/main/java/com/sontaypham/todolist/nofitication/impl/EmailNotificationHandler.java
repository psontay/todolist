package com.sontaypham.todolist.nofitication.impl;

import com.sontaypham.todolist.dto.request.NotificationRequest;
import com.sontaypham.todolist.entities.EmailDetails;
import com.sontaypham.todolist.enums.NotificationChannel;
import com.sontaypham.todolist.nofitication.NotificationHandler;
import com.sontaypham.todolist.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationHandler implements NotificationHandler {

    private final EmailService emailService;

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public void send(NotificationRequest request) {
        EmailDetails details = EmailDetails.builder()
                                           .to(request.getTo())
                                           .subject(request.getSubject())
                                           .messageBody(request.getMessageBody())
                                           .templateName(request.getTemplateName())
                                           .attachmentPaths(request.getAttachmentPaths())
                                           .variables(request.getVariables())
                                           .build();

        if (request.getTemplateName() != null) {
            emailService.sendTemplateHtmlEmail(details);
        } else {
            emailService.sendTextEmail(details);
        }
    }

}
