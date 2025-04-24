package com.eighttoten.member.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber,String> {
    private static final String PHONE_NUMBER_PATTERN = "^(01[016789]{1})[0-9]{3,4}[0-9]{4}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        return  value.matches(PHONE_NUMBER_PATTERN);
    }
}