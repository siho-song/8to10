package com.eighttoten.validator.schedule.objecterror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.eighttoten.dto.schedule.request.DateRangeValidatable;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, DateRangeValidatable> {

    @Override
    public boolean isValid(DateRangeValidatable value, ConstraintValidatorContext context) {
        return value.takeStartDateTime().isBefore(value.takeEndDateTime());
    }
}