package com.sontaypham.todolist.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank( message = "required name!")
    String name;
    @NotBlank( message = "required message!")
    String password;
    @Email( message = "not an email type!")
    String email;
}
