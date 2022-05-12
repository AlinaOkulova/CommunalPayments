package com.example.communalpayments.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckCardNumberValidator.class)
public @interface CheckCardNumber {

    String message() default "Номер карты введен неправильно";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
