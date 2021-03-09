package com.github.suloginscene.authserver.jwt.api;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.config.RestDocsConfig;
import com.github.suloginscene.authserver.testing.db.RepositoryProxy;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
import com.github.suloginscene.jjwthelper.JwtReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.authserver.testing.api.RequestBuilder.ofPost;
import static com.github.suloginscene.authserver.testing.api.ResultMatcherFactory.jwtAudienceIs;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs @Import(RestDocsConfig.class)
@DisplayName("JWT 발급 API")
public class JwtRestControllerTest {

    static final String URL = linkTo(JwtRestController.class).toString();

    @Autowired MockMvc mockMvc;
    @Autowired JwtReader jwtReader;
    @Autowired RepositoryProxy repositoryProxy;

    Member member;
    String email;
    String password;


    @BeforeEach
    void setup() {
        member = DefaultMembers.create();
        email = DefaultMembers.EMAIL_VALUE;
        password = DefaultMembers.RAW_PASSWORD_VALUE;
    }

    @AfterEach
    void clear() {
        repositoryProxy.clear();
    }


    @Test
    @DisplayName("정상 - aud 싣은 JWT 발급")
    void authServer_onSuccess_returnsAccessToken() throws Exception {
        repositoryProxy.given(member);

        JwtRequest request = new JwtRequest(email, password);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        ResultActions then = when.andExpect(
                jwtAudienceIs(jwtReader, member.getId()));

        then.andDo(document("jwt"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 - 400")
    void authServer_withNonExistentUsername_returns400() throws Exception {
        repositoryProxy.given(member);

        JwtRequest request = new JwtRequest("non-existent@email.com", password);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 비밀번호 - 400")
    void authServer_withWrongPassword_returns400() throws Exception {
        repositoryProxy.given(member);

        JwtRequest request = new JwtRequest(email, "wrongPassword");
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 null - 400")
    void authServer_withNullEmail_returns400() throws Exception {
        repositoryProxy.given(member);

        JwtRequest request = new JwtRequest(null, password);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호 null - 400")
    void authServer_withNullPassword_returns400() throws Exception {
        repositoryProxy.given(member);

        JwtRequest request = new JwtRequest(email, null);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

}
