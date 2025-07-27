package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.EmailResponse;
import com.sontaypham.todolist.Entities.EmailDetails;
import com.sontaypham.todolist.Service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/sendSimpleMail")
    public ApiResponse<EmailResponse> sendEmail(@RequestBody EmailDetails emailDetails) {
        try {
            return ApiResponse.<EmailResponse>builder().status(1).message("Sent email success!").data(emailService.sendSimpleMail(emailDetails)).build();
        }catch (Exception e){
            log.error(e.getMessage());
            return ApiResponse.<EmailResponse>builder().status(0).message("Sent email failed").data(new EmailResponse(false , "Internal server error")).build();
        }
    }
}
