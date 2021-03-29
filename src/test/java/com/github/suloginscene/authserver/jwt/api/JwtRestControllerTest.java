package com.github.suloginscene.authserver.jwt.api;

import com.github.suloginscene.authserver.jwt.api.request.JwtRequest;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.base.ControllerTest;
import com.github.suloginscene.authserver.testing.data.TestingMembers;
import com.github.suloginscene.jwt.JwtReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.github.suloginscene.authserver.testing.data.TestingMembers.EMAIL_VALUE;
import static com.github.suloginscene.authserver.testing.data.TestingMembers.RAW_PASSWORD_VALUE;
import static com.github.suloginscene.test.RequestBuilder.ofPost;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("JWT 발급 API")
public class JwtRestControllerTest extends ControllerTest {

    static final String URL = linkTo(JwtRestController.class).toString();

    @Autowired JwtReader jwtReader;


    @Test
    @DisplayName("정상 - aud 싣은 JWT 발급")
    void authServer_onSuccess_returnsAccessToken() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        JwtRequest request = new JwtRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        ResultActions then = when.andExpect(status().isOk())
                .andExpect(jwtAudienceIs(member.getId()));

        then.andDo(document("jwt"));
    }

    private ResultMatcher jwtAudienceIs(Long id) {
        return (result) -> {
            String encodedJwt = result.getResponse().getContentAsString();
            String actualAudience = jwtReader.getAudience(encodedJwt);

            AssertionErrors.assertEquals("Audience", id, Long.parseLong(actualAudience));
        };
    }

    @Test
    @DisplayName("존재하지 않는 사용자 - 404")
    void authServer_withNonExistentUsername_returns404() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        JwtRequest request = new JwtRequest("non-existent@email.com", RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("잘못된 비밀번호 - 400")
    void authServer_withWrongPassword_returns400() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        JwtRequest request = new JwtRequest(EMAIL_VALUE, "wrongPassword");
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 null - 400")
    void authServer_withNullEmail_returns400() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        JwtRequest request = new JwtRequest(null, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호 null - 400")
    void authServer_withNullPassword_returns400() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        JwtRequest request = new JwtRequest(EMAIL_VALUE, null);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

}
