package com.github.suloginscene.authserver.filter;

import com.github.suloginscene.authserver.config.JwtProperties;
import com.github.suloginscene.authserver.member.api.MemberRestController;
import com.github.suloginscene.authserver.member.api.SignupRequest;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.db.RepositoryFacade;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
import com.github.suloginscene.jjwthelper.JwtFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.authserver.testing.api.RequestBuilder.ofGet;
import static com.github.suloginscene.authserver.testing.api.RequestBuilder.ofPost;
import static com.github.suloginscene.authserver.testing.api.RequestBuilder.ofPreflight;
import static com.github.suloginscene.authserver.testing.fixture.DefaultMembers.EMAIL_VALUE;
import static com.github.suloginscene.authserver.testing.fixture.DefaultMembers.RAW_PASSWORD_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("CORS 필터")
public class CorsFilterTest {

    static final String URL = linkTo(MemberRestController.class).toString();

    @Autowired MockMvc mockMvc;
    @Autowired JwtProperties jwtProperties;
    @Autowired JwtFactory jwtFactory;
    @Autowired RepositoryFacade repositoryFacade;

    String validOrigin;
    String invalidOrigin;

    SignupRequest signupRequest;
    Member member;


    @BeforeEach
    void setup() {
        validOrigin = jwtProperties.getUrls().split(",")[0];
        invalidOrigin = "http://invalid.com";

        signupRequest = new SignupRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        member = DefaultMembers.create();
    }

    @AfterEach
    void clear() {
        repositoryFacade.clear();
    }


    @Test
    @DisplayName("OPTIONS 성공 - 200")
    void options_fromValidOrigin_returns200() throws Exception {
        ResultActions when = mockMvc.perform(
                ofPreflight(URL, validOrigin, POST, CONTENT_TYPE, ACCEPT).build());

        when.andExpect(status().isOk());
    }

    @Test
    @DisplayName("OPTIONS 실패 - 403")
    void options_fromInvalidOrigin_returns403() throws Exception {
        ResultActions when = mockMvc.perform(
                ofPreflight(URL, invalidOrigin, POST, CONTENT_TYPE, ACCEPT).build());

        when.andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("POST 성공 - 201")
    void signup_fromValidOrigin_returns201() throws Exception {
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(signupRequest).origin(validOrigin).build());

        when.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST 실패 - 403")
    void signup_fromInvalidOrigin_returns403() throws Exception {
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(signupRequest).origin(invalidOrigin).build());

        when.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET 성공 - 200")
    void getMember_fromValidOrigin_returns200() throws Exception {
        repositoryFacade.given(member);
        String jwt = jwtFactory.create(member.getId());

        ResultActions when = mockMvc.perform(
                ofGet(URL + "/" + member.getId()).jwt(jwt).origin(validOrigin).build());

        when.andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET 실패 - 403")
    void getMember_fromInvalidOrigin_returns403() throws Exception {
        repositoryFacade.given(member);
        String jwt = jwtFactory.create(member.getId());

        ResultActions when = mockMvc.perform(
                ofGet(URL + "/" + member.getId()).jwt(jwt).origin(invalidOrigin).build());

        when.andExpect(status().isForbidden());
    }

}
