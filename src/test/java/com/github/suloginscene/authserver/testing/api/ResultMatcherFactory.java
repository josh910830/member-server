package com.github.suloginscene.authserver.testing.api;

import com.github.suloginscene.jjwthelper.JwtReader;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.ResultMatcher;


public class ResultMatcherFactory {

    public static ResultMatcher jwtAudienceIs(JwtReader jwtReader, Long id) {
        return (result) -> {
            String encodedJwt = result.getResponse().getContentAsString();
            String actualAudience = jwtReader.getAudience(encodedJwt);

            AssertionErrors.assertEquals("Audience", id, Long.parseLong(actualAudience));
        };
    }

}
