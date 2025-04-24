package com.eighttoten.schedule.validator.fielderror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FrequencyValidator implements ConstraintValidator<Frequency,String> {
    private static final String SCHEDULE_FREQUENCY_PATTERN = "^(weekly|daily|biweekly)$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }
        return value.matches(SCHEDULE_FREQUENCY_PATTERN);
    }
}