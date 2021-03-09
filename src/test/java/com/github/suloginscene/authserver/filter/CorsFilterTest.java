package com.github.suloginscene.authserver.filter;

import com.github.suloginscene.authserver.config.JwtProperties;
import com.github.suloginscene.authserver.jjwthelper.JwtFactory;
import com.github.suloginscene.authserver.member.api.MemberRestController;
import com.github.suloginscene.authserver.member.api.SignupRequest;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.db.RepositoryProxy;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
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
    @Autowired RepositoryProxy repositoryProxy;

    String validOrigin;
    String invalidOrigin;

    String email;
    String password;
    Member member;


    @BeforeEach
    void setup() {
        validOrigin = jwtProperties.getUrls().split(",")[0];
        invalidOrigin = "http://invalid.com";

        email = DefaultMembers.EMAIL;
        password = DefaultMembers.RAW_PASSWORD;
        member = DefaultMembers.create();
    }

    @AfterEach
    void clear() {
        repositoryProxy.clear();
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
                ofPreflight(URL, invalidOrigin, POST).build());

        when.andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("POST 성공 - 201")
    void signup_fromValidOrigin_returns201() throws Exception {
        SignupRequest request = new SignupRequest(email, password);
        ResultActions when = mockMvc.perform(
                ofPost(URL).attachJson(request).attachOrigin(validOrigin).build());

        when.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST 실패 - 403")
    void signup_fromInvalidOrigin_returns403() throws Exception {
        SignupRequest request = new SignupRequest(email, password);
        ResultActions when = mockMvc.perform(
                ofPost(URL).attachJson(request).attachOrigin(invalidOrigin).build());

        when.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET 성공 - 200")
    void getMember_fromValidOrigin_returns200() throws Exception {
        repositoryProxy.given(member);
        String jwt = jwtFactory.of(member.getId());

        ResultActions when = mockMvc.perform(
                ofGet(URL + "/" + member.getId()).attachJwt(jwt).attachOrigin(validOrigin).build());

        when.andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET 실패 - 403")
    void getMember_fromInvalidOrigin_returns403() throws Exception {
        repositoryProxy.given(member);
        String jwt = jwtFactory.of(member.getId());

        ResultActions when = mockMvc.perform(
                ofGet(URL + "/" + member.getId()).attachJwt(jwt).attachOrigin(invalidOrigin).build());

        when.andExpect(status().isForbidden());
    }

}
