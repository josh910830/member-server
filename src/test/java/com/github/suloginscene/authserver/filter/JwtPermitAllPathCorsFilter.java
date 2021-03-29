package com.github.suloginscene.authserver.filter;

import com.github.suloginscene.authserver.member.api.SignupRequest;
import com.github.suloginscene.authserver.testing.db.RepositoryFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.authserver.testing.fixture.DefaultMembers.EMAIL_VALUE;
import static com.github.suloginscene.authserver.testing.fixture.DefaultMembers.RAW_PASSWORD_VALUE;
import static com.github.suloginscene.test.RequestBuilder.ofPost;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("JWT 허용 경로 CORS 필터")
public class JwtPermitAllPathCorsFilter {

    @Autowired MockMvc mockMvc;
    @Autowired RepositoryFacade repositoryFacade;

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

    @AfterEach
    void clear() {
        repositoryFacade.clear();
    }


    @Test
    @DisplayName("가입 (허용출처) - 201")
    void postMember_fromValidOrigin_returns201() throws Exception {
        SignupRequest request = new SignupRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).origin(validOrigin).json(request).build());

        when.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("가입 (비허용 출처) - 403")
    void postMember_fromInvalidOrigin_returns403() throws Exception {
        SignupRequest request = new SignupRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).origin(invalidOrigin).json(request).build());

        when.andExpect(status().isForbidden());
    }

}
