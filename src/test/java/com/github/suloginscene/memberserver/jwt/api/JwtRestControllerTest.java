package com.github.suloginscene.memberserver.jwt.api;

import com.github.suloginscene.jwt.JwtReader;
import com.github.suloginscene.memberserver.jwt.domain.RefreshToken;
import com.github.suloginscene.memberserver.member.domain.Member;
import com.github.suloginscene.memberserver.testing.base.ControllerTest;
import com.github.suloginscene.memberserver.testing.data.TestingMembers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Map;

import static com.github.suloginscene.memberserver.testing.data.TestingMembers.EMAIL_VALUE;
import static com.github.suloginscene.memberserver.testing.data.TestingMembers.RAW_PASSWORD_VALUE;
import static com.github.suloginscene.test.RequestBuilder.ofPost;
import static com.github.suloginscene.test.ResultParser.toResponseAsJsonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("JWT 발급 API")
public class JwtRestControllerTest extends ControllerTest {

    static final String URL = linkTo(JwtRestController.class).toString();

    @Autowired JwtReader jwtReader;


    @Test
    @DisplayName("발급 - 200(access_token & refresh_token)")
    void issue_onSuccess_returnsAccessTokenAndRefreshToken() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        JwtRequest request = new JwtRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        ResultActions then = when.andExpect(status().isOk())
                .andExpect(exists("access_token"))
                .andExpect(exists("refresh_token"))
                .andExpect(jwtAudienceIs(member.getId()));

        then.andDo(document("issue-jwt"));
    }

    @Test
    @DisplayName("발급(이메일 null) - 400")
    void issue_withNullEmail_returns400() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        JwtRequest request = new JwtRequest(null, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("발급(비밀번호 null) - 400")
    void issue_withNullPassword_returns400() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        JwtRequest request = new JwtRequest(EMAIL_VALUE, null);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("갱신 - 200(access_token & refresh_token)")
    void renew_onSuccess_returnsAccessTokenAndRefreshToken() throws Exception {
        Long memberId = 1L;
        int expDays = 1;
        RefreshToken refreshToken = RefreshToken.of(memberId, expDays);
        given(refreshToken);

        ResultActions when = mockMvc.perform(
                ofPost(URL + "/renew").body(refreshToken.getValue()).build());

        ResultActions then = when.andExpect(status().isOk())
                .andExpect(exists("access_token"))
                .andExpect(exists("refresh_token"))
                .andExpect(jwtAudienceIs(memberId));

        then.andDo(document("renew-jwt"));
    }

    @Test
    @DisplayName("갱신(리프레시토큰 null) - 400")
    void renew_withNullRefreshToken_returnsAccessTokenAndRefreshToken() throws Exception {
        ResultActions when = mockMvc.perform(
                ofPost(URL + "/renew").build());

        when.andExpect(status().isBadRequest());
    }


    private ResultMatcher exists(String key) {
        return (result) -> {
            Map<String, Object> jsonMap = toResponseAsJsonMap(result);
            Object value = jsonMap.get(key);
            assertThat(value).isNotNull();
        };
    }

    private ResultMatcher jwtAudienceIs(Long id) {
        return (result) -> {
            String encodedJwt = toResponseAsJsonMap(result).get("access_token").toString();
            String actualAudience = jwtReader.getAudience(encodedJwt);
            assertThat(Long.parseLong(actualAudience)).isEqualTo(id);
        };
    }

}
