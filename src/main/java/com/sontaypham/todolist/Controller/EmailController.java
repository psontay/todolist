package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.EmailResponse;
import com.sontaypham.todolist.Entities.EmailDetails;
import com.sontaypham.todolist.Service.EmailService;
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

  public EmailController(EmailService emailService) {
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
