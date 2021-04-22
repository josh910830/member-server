package com.github.suloginscene.memberserver.jwt.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.suloginscene.memberserver.jwt.application.TokenPair;
import lombok.Data;


@Data
public class JwtResponse {

    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("refresh_token")
    private final String refreshToken;


    JwtResponse(TokenPair tokenPair) {
        accessToken = tokenPair.getJwt();
        refreshToken = tokenPair.getRefreshToken();
    }

}
