package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.member.api.request.MemberPasswordChangeRequest;
import com.github.suloginscene.authserver.member.api.request.MemberSignupRequest;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.base.ControllerTest;
import com.github.suloginscene.authserver.testing.data.TestingMembers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.authserver.testing.data.TestingMembers.EMAIL_VALUE;
import static com.github.suloginscene.authserver.testing.data.TestingMembers.RAW_PASSWORD_VALUE;
import static com.github.suloginscene.test.RequestBuilder.ofGet;
import static com.github.suloginscene.test.RequestBuilder.ofPost;
import static com.github.suloginscene.test.RequestBuilder.ofPut;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("회원 API")
class MemberRestControllerTest extends ControllerTest {

    static final String URL = linkTo(MemberRestController.class).toString();


    @Test
    @DisplayName("회원가입 성공 - 201")
    void signup_onSuccess_returns201() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        ResultActions then = when.andExpect(status().isCreated());

        then.andDo(document("post-member"));
    }

    @Test
    @DisplayName("회원가입 실패(이메일 null) - 400")
    void signup_withNullEmail_returns400() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(null, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패(비밀번호 null) - 400")
    void signup_withNullPassword_returns400() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(EMAIL_VALUE, null);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패(이메일 형식) - 400")
    void signup_withInvalidEmail_returns400() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest("notEmail", RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패(비밀번호 길이) - 400")
    void signup_withInvalidPassword_returns400() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(EMAIL_VALUE, "short");
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패(이메일 중복) - 400")
    void signup_onDuplicate_returns400() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        MemberSignupRequest request = new MemberSignupRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        when.andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("내 정보 조회 성공 - 200")
    void myInfo_onSuccess_returns200() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        String jwt = jwtFactory.create(member.getId());

        ResultActions when = mockMvc.perform(
                ofGet(URL).jwt(jwt).build());

        ResultActions then = when.andExpect(status().isOk());

        then.andDo(document("get-member"));
    }


    @Test
    @DisplayName("비밀번호 변경 성공 - 204")
    void changePassword_onSuccess_returns204() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        String jwt = jwtFactory.create(member.getId());

        MemberPasswordChangeRequest request = new MemberPasswordChangeRequest("newPassword");
        ResultActions when = mockMvc.perform(
                ofPut(URL).jwt(jwt).json(request).build());

        ResultActions then = when.andExpect(status().isNoContent());

        then.andDo(document("put-member-password"));
    }

    @Test
    @DisplayName("비밀번호 변경 실패(비밀번호 null) - 400")
    void changePassword_withNullPassword_returns400() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        String jwt = jwtFactory.create(member.getId());

        MemberPasswordChangeRequest request = new MemberPasswordChangeRequest(null);
        ResultActions when = mockMvc.perform(
                ofPut(URL).jwt(jwt).json(request).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호 변경 실패(비밀번호 길이) - 400")
    void changePassword_withInvalidPassword_returns400() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        String jwt = jwtFactory.create(member.getId());

        MemberPasswordChangeRequest request = new MemberPasswordChangeRequest("short");
        ResultActions when = mockMvc.perform(
                ofPut(URL).jwt(jwt).json(request).build());

        when.andExpect(status().isBadRequest());
    }

}
