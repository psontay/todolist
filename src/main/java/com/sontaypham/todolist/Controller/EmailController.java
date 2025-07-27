package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.EmailResponse;
import com.sontaypham.todolist.Entities.EmailDetails;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/email")
public class EmailController {
    EmailService emailService;
    @PostMapping
    public ApiResponse<EmailResponse> sendEmail(@RequestBody EmailDetails emailDetails) {
        try {
            emailService.sendEmail(emailDetails);

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
