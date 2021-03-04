package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.api.JwtFactorySupporter;
import com.github.suloginscene.authserver.testing.api.RequestSupporter;
import com.github.suloginscene.authserver.testing.api.RestDocsConfig;
import com.github.suloginscene.authserver.testing.db.RepositoryProxy;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs @Import(RestDocsConfig.class)
@DisplayName("회원 API")
class MemberRestControllerTest {

    static final String URL = linkTo(MemberRestController.class).toString();

    @Autowired MockMvc mockMvc;
    @Autowired RequestSupporter requestSupporter;
    @Autowired JwtFactorySupporter jwtFactorySupporter;
    @Autowired RepositoryProxy repositoryProxy;

    String email;
    String password;
    Member member;


    @BeforeEach
    void setup() {
        email = DefaultMembers.EMAIL;
        password = DefaultMembers.RAW_PASSWORD;
        member = DefaultMembers.create();
    }

    @AfterEach
    void clear() {
        repositoryProxy.clear();
    }


    @Test
    @DisplayName("POST 성공 - 201")
    void signup_onSuccess_returns201() throws Exception {
        SignupRequest request = new SignupRequest(email, password);
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        ResultActions then = when.andExpect(status().isCreated());

        then.andDo(document("signup"));
    }

    @Test
    @DisplayName("POST 실패(이메일 null) - 400")
    void signup_withNullEmail_returns400() throws Exception {
        SignupRequest request = new SignupRequest(null, password);
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST 실패(비밀번호 null) - 400")
    void signup_withNullPassword_returns400() throws Exception {
        SignupRequest request = new SignupRequest(email, null);
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST 실패(이메일 형식) - 400")
    void signup_withInvalidEmail_returns400() throws Exception {
        SignupRequest request = new SignupRequest("notEmail", password);
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST 실패(비밀번호 길이) - 400")
    void signup_withInvalidPassword_returns400() throws Exception {
        SignupRequest request = new SignupRequest(email, "short");
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("POST 실패(이메일 중복) - 400")
    void signup_onDuplicate_returns400() throws Exception {
        repositoryProxy.given(member);

        SignupRequest request = new SignupRequest(email, password);
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("GET 성공 - 200")
    void getMember_onSuccess_returns200() throws Exception {
        repositoryProxy.given(member);
        String jwt = jwtFactorySupporter.create(member.getId());

        ResultActions when = mockMvc.perform(
                requestSupporter.getWithJwt(URL + "/" + member.getId(), jwt));

        ResultActions then = when.andExpect(status().isOk());

        then.andDo(document("get_member"));
    }

    @Test
    @DisplayName("GET 실패(권한) - 403")
    void getMember_withNotOwner_returns403() throws Exception {
        repositoryProxy.given(member);
        Long audience = member.getId() + 1;
        String jwt = jwtFactorySupporter.create(audience);

        ResultActions when = mockMvc.perform(
                requestSupporter.getWithJwt(URL + "/" + member.getId(), jwt));

        when.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET 실패(리소스 없음) - 404")
    void getMember_onNonExistent_returns404() throws Exception {
        Long nonExistentId = Long.MAX_VALUE;
        String jwt = jwtFactorySupporter.create(nonExistentId);

        ResultActions when = mockMvc.perform(
                requestSupporter.getWithJwt(URL + "/" + nonExistentId, jwt));

        when.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET 실패(JWT 만료) - 403")
    void getMember_withExpiredJwt_returns403() throws Exception {
        repositoryProxy.given(member);
        String jwt = jwtFactorySupporter.expired(member.getId());

        ResultActions when = mockMvc.perform(
                requestSupporter.getWithJwt(URL + "/" + member.getId(), jwt));

        when.andExpect(status().isForbidden())
                .andExpect(content().string(ExpiredJwtException.class.getSimpleName()));
    }

    @Test
    @DisplayName("GET 실패(JWT 서명) - 403")
    void getMember_withInvalidSignature_returns403() throws Exception {
        repositoryProxy.given(member);
        String jwt = jwtFactorySupporter.invalid(member.getId());

        ResultActions when = mockMvc.perform(
                requestSupporter.getWithJwt(URL + "/" + member.getId(), jwt));

        when.andExpect(status().isForbidden())
                .andExpect(content().string(SignatureException.class.getSimpleName()));
    }

    @Test
    @DisplayName("GET 실패(JWT 형식) - 403")
    void getMember_withMalformedJwt_returns403() throws Exception {
        repositoryProxy.given(member);
        String jwt = jwtFactorySupporter.malformed();

        ResultActions when = mockMvc.perform(
                requestSupporter.getWithJwt(URL + "/" + member.getId(), jwt));

        when.andExpect(status().isForbidden())
                .andExpect(content().string(MalformedJwtException.class.getSimpleName()));
    }

}
