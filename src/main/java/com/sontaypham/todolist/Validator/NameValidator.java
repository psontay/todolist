package com.sontaypham.todolist.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<NameConstraint, String> {
    @Override
    public void initialize(NameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid (String name , ConstraintValidatorContext context) {
        if ( name == null || name.isBlank() ) return true;
        return name.length() >= 3;
    }
}
