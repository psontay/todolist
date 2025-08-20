package com.sontaypham.todolist.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameConstraint, String> {
  @Override
  public void initialize(UsernameConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String name, ConstraintValidatorContext context) {
    if (name == null || name.isBlank()) return true;
    return name.length() >= 3;
  }
}
