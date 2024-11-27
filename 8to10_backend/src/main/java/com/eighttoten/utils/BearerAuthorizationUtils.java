package com.eighttoten.utils;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.InvalidTokenException;

public class BearerAuthorizationUtils {
    public static final String BEARER_CODE = "Bearer ";

    public String extractToken(String authHeader){
        if(authHeader != null && authHeader.startsWith(BEARER_CODE)){
            return authHeader.substring(BEARER_CODE.length()).trim();
        }
        throw new InvalidTokenException(ExceptionCode.INVALID_ACCESS_TOKEN);
    }
}