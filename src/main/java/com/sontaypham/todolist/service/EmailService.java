package com.sontaypham.todolist.service;

import com.sontaypham.todolist.dto.response.EmailResponse;
import com.sontaypham.todolist.entities.EmailDetails;

public interface EmailService {
    EmailResponse sendTextEmail(EmailDetails details);
    EmailResponse sendTemplateHtmlEmail(EmailDetails details);
    EmailResponse sendAttachmentEmail(EmailDetails details);
}
