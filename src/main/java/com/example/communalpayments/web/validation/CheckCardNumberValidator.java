package com.example.communalpayments.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckCardNumberValidator implements ConstraintValidator<CheckCardNumber, String> {
    @Override
    public void initialize(CheckCardNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        int sum = 0;
        boolean alternate = false;
        for (int i = s.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(s.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}
