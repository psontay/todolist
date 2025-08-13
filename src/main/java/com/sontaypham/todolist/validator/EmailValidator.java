package com.sontaypham.todolist.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {
  @Override
  public boolean isValid(String userMail, ConstraintValidatorContext context) {
    if (userMail == null || userMail.isBlank()) return true;
    return userMail.endsWith("@gmail.com") || userMail.endsWith(".edu.vn");
  }

  @Override
  public void initialize(EmailConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }
}
