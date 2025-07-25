package com.sontaypham.todolist.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {
    private String domain;

    @Override
    public boolean isValid(String userMail, ConstraintValidatorContext context) {
        if (userMail == null || userMail.isBlank()) return true;
        return userMail.startsWith(this.domain) && userMail.endsWith("@gmail.com");
    }

    @Override
    public void initialize(EmailConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.domain = constraintAnnotation.domain();
    }
}
