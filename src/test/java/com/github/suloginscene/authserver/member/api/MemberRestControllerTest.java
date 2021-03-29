package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.db.RepositoryFacade;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
import com.github.suloginscene.jwt.JwtFactory;
import com.github.suloginscene.test.RestDocsConfig;
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

import static com.github.suloginscene.test.RequestBuilder.ofGet;
import static com.github.suloginscene.test.RequestBuilder.ofPost;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs @Import(RestDocsConfig.class)
@DisplayName("회원 API")
class MemberRestControllerTest {

    static final String URL = linkTo(MemberRestController.class).toString();

    @Autowired MockMvc mockMvc;
    @Autowired JwtFactory jwtFactory;
    @Autowired RepositoryFacade repositoryFacade;

    String email;
    String password;
    Member member;


    @BeforeEach
    void setup() {
        email = DefaultMembers.EMAIL_VALUE;
        password = DefaultMembers.RAW_PASSWORD_VALUE;
        member = DefaultMembers.create();
    }

    @AfterEach
    void clear() {
        repositoryFacade.clear();
    }


    @Test
    @DisplayName("POST 성공 - 201")
    void signup_onSuccess_returns201() throws Exception {
        SignupRequest request = new SignupRequest(email, password);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        ResultActions then = when.andExpect(status().isCreated());

        then.andDo(document("signup"));
    }

    @Test
    @DisplayName("POST 실패(이메일 null) - 400")
    void signup_withNullEmail_returns400() throws Exception {
        SignupRequest request = new SignupRequest(null, password);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST 실패(비밀번호 null) - 400")
    void signup_withNullPassword_returns400() throws Exception {
        SignupRequest request = new SignupRequest(email, null);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST 실패(이메일 형식) - 400")
    void signup_withInvalidEmail_returns400() throws Exception {
        SignupRequest request = new SignupRequest("notEmail", password);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST 실패(비밀번호 길이) - 400")
    void signup_withInvalidPassword_returns400() throws Exception {
        SignupRequest request = new SignupRequest(email, "short");
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST 실패(이메일 중복) - 400")
    void signup_onDuplicate_returns400() throws Exception {
        repositoryFacade.given(member);

        SignupRequest request = new SignupRequest(email, password);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("GET 성공 - 200")
    void myInfo_onSuccess_returns200() throws Exception {
        repositoryFacade.given(member);
        String jwt = jwtFactory.create(member.getId());

        ResultActions when = mockMvc.perform(
                ofGet(URL).jwt(jwt).build());

        ResultActions then = when.andExpect(status().isOk());

        then.andDo(document("get_member"));
    }

    @Test
    @DisplayName("GET 실패(리소스 없음) - 404")
    void myInfo_onNonExistent_returns404() throws Exception {
        Long nonExistentId = Long.MAX_VALUE;
        String jwt = jwtFactory.create(nonExistentId);

        ResultActions when = mockMvc.perform(
                ofGet(URL).jwt(jwt).build());

        when.andExpect(status().isNotFound());
    }

}
