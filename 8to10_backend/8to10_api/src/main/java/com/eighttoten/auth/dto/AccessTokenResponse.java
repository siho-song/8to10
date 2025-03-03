package com.eighttoten.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AccessTokenResponse {
    private String accessToken;

    public static AccessTokenResponse of(String accessToken){
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.accessToken = accessToken;
        return accessTokenResponse;
    }
}