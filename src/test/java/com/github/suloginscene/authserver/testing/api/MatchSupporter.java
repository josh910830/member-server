package com.github.suloginscene.authserver.testing.api;

import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.ResultMatcher;


@Component
@RequiredArgsConstructor
public class MatchSupporter {

    private final JwtParser jwtParser;


    public ResultMatcher jwtAudienceIs(Long id) {
        return (result) -> {
            String encodedJwt = result.getResponse().getContentAsString();
            String actualAudience = jwtParser.parseClaimsJws(encodedJwt).getBody().getAudience();

            AssertionErrors.assertEquals("Audience", id, Long.parseLong(actualAudience));
        };
    }

}
