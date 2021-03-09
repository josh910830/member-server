package com.github.suloginscene.authserver.testing.api;

import com.github.suloginscene.jjwthelper.JwtReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.ResultMatcher;


@Component
@RequiredArgsConstructor
public class MatchSupporter {

    private final JwtReader jwtReader;


    public ResultMatcher jwtAudienceIs(Long id) {
        return (result) -> {
            String encodedJwt = result.getResponse().getContentAsString();
            String actualAudience = jwtReader.getAudience(encodedJwt);

            AssertionErrors.assertEquals("Audience", id, Long.parseLong(actualAudience));
        };
    }

}
