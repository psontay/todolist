package com.sontaypham.todolist.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordConstraint {
  String message() default "PASSWORD_TYPE_INVALID";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
