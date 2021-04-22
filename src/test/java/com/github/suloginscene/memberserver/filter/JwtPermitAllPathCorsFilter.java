package com.github.suloginscene.memberserver.filter;

import com.github.suloginscene.memberserver.member.api.request.MemberSignupRequest;
import com.github.suloginscene.memberserver.testing.base.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.memberserver.testing.data.TestingMembers.EMAIL_VALUE;
import static com.github.suloginscene.memberserver.testing.data.TestingMembers.RAW_PASSWORD_VALUE;
import static com.github.suloginscene.test.RequestBuilder.ofPost;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("JWT 허용 경로 CORS 필터")
public class JwtPermitAllPathCorsFilter extends ControllerTest {

    static final String URL = "/api/members";

    String validOrigin;
    String invalidOrigin;


    @BeforeEach
    void setup() {
        validOrigin = securityProperties.getOrigins().split(",")[0];
        invalidOrigin = "https://invalid.com";
    }


    @Test
    @DisplayName("가입 (허용출처) - 200")
    void postMember_fromValidOrigin_returns200() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).origin(validOrigin).json(request).build());

        when.andExpect(status().isOk());
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
