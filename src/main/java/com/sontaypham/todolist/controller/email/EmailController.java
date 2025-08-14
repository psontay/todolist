package com.sontaypham.todolist.controller.email;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.EmailResponse;
import com.sontaypham.todolist.entities.EmailDetails;
import com.sontaypham.todolist.service.EmailService;
import com.sontaypham.todolist.service.impl.EmailServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/email")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController extends BaseController {
  EmailService emailService;

  public EmailController(EmailServiceImpl emailService) {
    this.emailService = emailService;
  }

  @PostMapping("/sendSimpleMail")
  public ResponseEntity<ApiResponse<EmailResponse>> sendEmail(
      @RequestBody EmailDetails emailDetails) {
    try {
      return buildSuccessResponse("Sent email success!", emailService.sendSimpleMail(emailDetails));
    } catch (Exception e) {
      log.error(e.getMessage());
      return buildErrorResponse(
          "Sent email failed", new EmailResponse(false, "Internal server error"));
    }
  }
}
