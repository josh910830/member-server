package com.github.suloginscene.memberserver.jwt.api;

import com.github.suloginscene.jwt.JwtReader;
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
    @DisplayName("정상 - access_token & refresh_token 발급")
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


    @Test
    @DisplayName("이메일 null - 400")
    void issue_withNullEmail_returns400() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        JwtRequest request = new JwtRequest(null, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호 null - 400")
    void issue_withNullPassword_returns400() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        JwtRequest request = new JwtRequest(EMAIL_VALUE, null);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

}
