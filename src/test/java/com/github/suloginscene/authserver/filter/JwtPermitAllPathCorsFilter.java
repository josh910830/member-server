package com.github.suloginscene.authserver.filter;

import com.github.suloginscene.authserver.member.api.request.MemberSignupRequest;
import com.github.suloginscene.authserver.testing.base.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.authserver.testing.data.TestingMembers.EMAIL_VALUE;
import static com.github.suloginscene.authserver.testing.data.TestingMembers.RAW_PASSWORD_VALUE;
import static com.github.suloginscene.test.RequestBuilder.ofPost;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("JWT 허용 경로 CORS 필터")
public class JwtPermitAllPathCorsFilter extends ControllerTest {

    static final String URL = "/api/members";

    @Value("${jwt.urls}")
    String urls;

    String validOrigin;
    String invalidOrigin;

    @BeforeEach
    void setup() {
        validOrigin = urls.split(",")[0];
        invalidOrigin = "http://invalid.com";
    }


    @Test
    @DisplayName("가입 (허용출처) - 201")
    void postMember_fromValidOrigin_returns201() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).origin(validOrigin).json(request).build());

        when.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("가입 (비허용 출처) - 403")
    void postMember_fromInvalidOrigin_returns403() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).origin(invalidOrigin).json(request).build());

        when.andExpect(status().isForbidden());
    }

}