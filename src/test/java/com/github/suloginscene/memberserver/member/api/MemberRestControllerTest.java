package com.github.suloginscene.memberserver.member.api;

import com.github.suloginscene.memberserver.member.api.request.MemberOnForgetPasswordRequest;
import com.github.suloginscene.memberserver.member.api.request.MemberPasswordChangeRequest;
import com.github.suloginscene.memberserver.member.api.request.MemberSignupRequest;
import com.github.suloginscene.memberserver.member.api.request.MemberVerificationRequest;
import com.github.suloginscene.memberserver.member.domain.Member;
import com.github.suloginscene.memberserver.member.domain.temp.TempMember;
import com.github.suloginscene.memberserver.testing.base.ControllerTest;
import com.github.suloginscene.memberserver.testing.data.TestingMembers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.memberserver.testing.data.TestingMembers.EMAIL_VALUE;
import static com.github.suloginscene.memberserver.testing.data.TestingMembers.RAW_PASSWORD_VALUE;
import static com.github.suloginscene.test.RequestBuilder.ofDelete;
import static com.github.suloginscene.test.RequestBuilder.ofGet;
import static com.github.suloginscene.test.RequestBuilder.ofPost;
import static com.github.suloginscene.test.RequestBuilder.ofPut;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("회원 API")
class MemberRestControllerTest extends ControllerTest {

    static final String URL = linkTo(MemberRestController.class).toString();


    @Test
    @DisplayName("회원가입 성공 - 200")
    void signup_onSuccess_returns200() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(EMAIL_VALUE, RAW_PASSWORD_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL).json(request).build());

        ResultActions then = when.andExpect(status().isOk())
                .andExpect(jsonPath("_links.verify").exists());

        then.andDo(document("signup"));
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
    @DisplayName("인증 - 201")
    void verify_onSuccess_returns201() throws Exception {
        TempMember tempMember = TestingMembers.temp();
        given(tempMember);

        Long id = tempMember.getId();
        String token = tempMember.getVerificationToken();
        MemberVerificationRequest request = new MemberVerificationRequest(token);
        ResultActions when = mockMvc.perform(
                ofPost(URL + "/verify/" + id).json(request).build());

        ResultActions then = when.andExpect(status().isCreated())
                .andExpect(header().exists("location"));

        then.andDo(document("verify"));
    }

    @Test
    @DisplayName("인증(요청 본문 없음) - 400")
    void verify_withNoRequestBody_returns400() throws Exception {
        TempMember tempMember = TestingMembers.temp();
        given(tempMember);

        Long id = tempMember.getId();
        ResultActions when = mockMvc.perform(
                ofPost(URL + "/verify/" + id).build());

        when.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("인증(토큰 null) - 400")
    void verify_withNullToken_returns400() throws Exception {
        TempMember tempMember = TestingMembers.temp();
        given(tempMember);

        Long id = tempMember.getId();
        MemberVerificationRequest request = new MemberVerificationRequest(null);
        ResultActions when = mockMvc.perform(
                ofPost(URL + "/verify/" + id).json(request).build());

        when.andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("내 정보 조회 성공 - 200")
    void myInfo_onSuccess_returns200() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        String jwt = jwtFactory.create(member.getId());

        ResultActions when = mockMvc.perform(
                ofGet(URL + "/my-info").jwt(jwt).build());

        ResultActions then = when.andExpect(status().isOk())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("_links.changePassword").exists())
                .andExpect(jsonPath("_links.withdraw").exists());

        then.andDo(document("my-info"));
    }


    @Test
    @DisplayName("비밀번호 분실 처리 - 200")
    void onForgetPassword_onSuccess_returns200() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        MemberOnForgetPasswordRequest request = new MemberOnForgetPasswordRequest(EMAIL_VALUE);
        ResultActions when = mockMvc.perform(
                ofPost(URL + "/on-forget-password").json(request).build());

        ResultActions then = when.andExpect(status().isOk());

        then.andDo(document("on-forget-password"));
    }

    @Test
    @DisplayName("비밀번호 분실 처리(요청 본문 없음) - 400")
    void onForgetPassword_withNoQueryString_returns400() throws Exception {
        ResultActions when = mockMvc.perform(
                ofPost(URL + "/on-forget-password").build());

        when.andExpect(status().isBadRequest());
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

        then.andDo(document("change-password"));
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

    @Test
    @DisplayName("회원탈퇴 성공 - 204")
    void withdraw_onSuccess_returns204() throws Exception {
        Member member = TestingMembers.create();
        given(member);

        String jwt = jwtFactory.create(member.getId());

        ResultActions when = mockMvc.perform(
                ofDelete(URL).jwt(jwt).build());

        ResultActions then = when.andExpect(status().isNoContent());

        then.andDo(document("withdraw"));
    }

}
