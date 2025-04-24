package com.eighttoten.member.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NicknameValidator implements ConstraintValidator<Nickname,String> {
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }

        if (NICKNAME_PATTERN.matcher(value).matches()) {
            return value.length() >= 2 && value.length() <= 12;
        } else {
            return false;
        }
    }
}