package show.schedulemanagement.validator.schedule.filedError;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DayValidator implements ConstraintValidator<Day,String> {
    String SCHEDULE_DAY_PATTERN = "^(mo|tu|we|th|fi|sa|su)$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(SCHEDULE_DAY_PATTERN);
    }
}