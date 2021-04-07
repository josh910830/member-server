package com.github.suloginscene.authserver;

import com.github.suloginscene.authserver.jwt.api.request.JwtRequest;
import com.github.suloginscene.authserver.member.api.request.MemberOnForgetPasswordRequest;
import com.github.suloginscene.authserver.member.api.request.MemberPasswordChangeRequest;
import com.github.suloginscene.authserver.member.api.request.MemberSignupRequest;
import com.github.suloginscene.authserver.member.api.request.MemberVerificationRequest;
import com.github.suloginscene.jwt.JwtFactory;
import com.github.suloginscene.mail.MailMessage;
import com.github.suloginscene.mail.Mailer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.github.suloginscene.test.RequestBuilder.ofDelete;
import static com.github.suloginscene.test.RequestBuilder.ofGet;
import static com.github.suloginscene.test.RequestBuilder.ofPost;
import static com.github.suloginscene.test.RequestBuilder.ofPut;
import static com.github.suloginscene.test.ResultParser.toResponseAsJsonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * 서비스 전체를 표현하기 위한 시나리오 테스트입니다.
 * 행복한 경로에 대해서만 작성되었고, 테스트 메서드들은 순차적으로 관계되어 있습니다.
 * 개별 클래스의 동작은 해당 클래스의 테스트 코드에 표현되어 있습니다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("인수 시나리오 테스트")
public class AcceptanceScenarioTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtFactory jwtFactory;
    @SpyBean Mailer mailer;

    String email = "test@email.com";
    String password = "password";


    // 프런트엔드에서의 _links 참조에 해당합니다.
    Map<String, String> relPathMap = new HashMap<>();

    // 이메일 인증을 위해 프런트엔드에 임시로 저장합니다.
    Long id; // 가입 신청 응답 본문으로 받습니다.
    String token; // 메일로 받습니다.

    // 메일을 담기 위한 임시 저장소입니다.
    ThreadLocal<MailMessage> tempMail = new ThreadLocal<>();

    // void mailer.send()를 프록시처럼 스텁하기 위해 사용됩니다.
    Answer<Void> proxiedVoid = invocation -> {
        MailMessage mail = invocation.getArgument(0);
        tempMail.set(mail);
        invocation.callRealMethod();
        return null;
    };

    // 가입 완료 후 발급받아 프런트엔드에 저장해두고 사용합니다.
    String jwt;


    @Order(1)
    @Test
    @DisplayName("인덱스 - 200: 링크 4개")
    void index() throws Exception {
        String url = "/api";

        ResultActions getIndex = mockMvc.perform(ofGet(url).build());

        MvcResult result = getIndex.andExpect(status().isOk()).andReturn();

        setRelPathMap(result);
        assertThat(relPathMap.get("signup")).isEqualTo("/api/members");
        assertThat(relPathMap.get("issueJwt")).isEqualTo("/jwt");
        assertThat(relPathMap.get("myInfo")).isEqualTo("/api/members/my-info");
        assertThat(relPathMap.get("onForgetPassword")).isEqualTo("/api/members/on-forget-password");
    }


    @Order(2)
    @Test
    @DisplayName("가입 신청 - 200: 인증 메일 발송")
    void signup() throws Exception {
        doAnswer(proxiedVoid).when(mailer).send(any());

        String url = relPathMap.get("signup");

        MemberSignupRequest request = new MemberSignupRequest(email, password);
        ResultActions signup = mockMvc.perform(ofPost(url).json(request).build());

        MvcResult result = signup.andExpect(status().isOk()).andReturn();

        setRelPathMap(result);
        assertThat(relPathMap.get("verify")).isEqualTo("/api/members/verify");

        then(mailer).should().send(any());
        assertThat(tempMail.get().getRecipient()).isEqualTo(email);
        assertThat(tempMail.get().getTitle()).isEqualTo("[Scene] 회원가입 인증 메일");
        assertThat(tempMail.get().getContent()).startsWith("회원가입 인증 토큰: ");

        id = Long.parseLong(toResponseAsJsonMap(result).get("id").toString());
        token = tempMail.get().getContent().split(": ")[1];
    }

    @Order(3)
    @Test
    @DisplayName("메일 인증 - 201")
    void verify() throws Exception {
        String url = relPathMap.get("verify");

        MemberVerificationRequest request = new MemberVerificationRequest(id, token);
        ResultActions verify = mockMvc.perform(ofPost(url).json(request).build());

        verify.andExpect(status().isCreated());
    }

    @Order(4)
    @Test
    @DisplayName("비밀번호 분실 - 200: 메일 발송")
    void onForgetPassword() throws Exception {
        doAnswer(proxiedVoid).when(mailer).send(any());

        String url = relPathMap.get("onForgetPassword");

        MemberOnForgetPasswordRequest request = new MemberOnForgetPasswordRequest(email);
        ResultActions onForgetPassword = mockMvc.perform(ofPost(url).json(request).build());

        onForgetPassword.andExpect(status().isOk());

        then(mailer).should().send(any());
        assertThat(tempMail.get().getRecipient()).isEqualTo(email);
        assertThat(tempMail.get().getTitle()).isEqualTo("[Scene] 랜덤 비밀번호 안내");
        assertThat(tempMail.get().getContent()).startsWith("새 비밀번호: ");

        password = tempMail.get().getContent().split(": ")[1];
    }


    @Order(5)
    @Test
    @DisplayName("JWT 발급 - 200")
    void issueJwt() throws Exception {
        String url = relPathMap.get("issueJwt");

        JwtRequest request = new JwtRequest(email, password);
        ResultActions issueJwt = mockMvc.perform(ofPost(url).json(request).build());

        MvcResult result = issueJwt.andExpect(status().isOk()).andReturn();

        String resultBody = result.getResponse().getContentAsString();
        assertThat(resultBody).matches(".+\\..+\\..+");

        jwt = resultBody;
    }


    @Order(6)
    @Test
    @DisplayName("내 정보 - 200")
    void myInfo() throws Exception {
        String url = relPathMap.get("myInfo");

        ResultActions getMyInfo = mockMvc.perform(ofGet(url).jwt(jwt).build());

        MvcResult result = getMyInfo.andExpect(status().isOk()).andReturn();

        setRelPathMap(result);
        assertThat(toResponseAsJsonMap(result).get("email")).isEqualTo("test@email.com");
        assertThat(relPathMap.get("changePassword")).isEqualTo("/api/members");
        assertThat(relPathMap.get("withdraw")).isEqualTo("/api/members");
    }

    @Order(7)
    @Test
    @DisplayName("비밀번호 변경 - 200")
    void changePassword() throws Exception {
        String url = relPathMap.get("changePassword");

        password = "newPassword";
        MemberPasswordChangeRequest request = new MemberPasswordChangeRequest(password);
        ResultActions changePassword = mockMvc.perform(ofPut(url).jwt(jwt).json(request).build());

        changePassword.andExpect(status().isNoContent());

        mockMvc.perform(ofPost(relPathMap.get("issueJwt")).json(new JwtRequest(email, password)).build())
                .andExpect(status().isOk());
    }

    @Order(8)
    @Test
    @DisplayName("탈퇴 - 204")
    void withdraw() throws Exception {
        String url = relPathMap.get("withdraw");

        ResultActions withdraw = mockMvc.perform(ofDelete(url).jwt(jwt).build());

        withdraw.andExpect(status().isNoContent());

        mockMvc.perform(ofPost(relPathMap.get("issueJwt")).json(new JwtRequest(email, password)).build())
                .andExpect(status().isNotFound());
    }


    private void setRelPathMap(MvcResult mvcResult) throws UnsupportedEncodingException {
        Map<String, Object> resultMap = toResponseAsJsonMap(mvcResult);
        Map<String, Object> links = (Map<String, Object>) resultMap.get("_links");
        links.forEach((rel, value) -> {
            String href = ((Map<String, String>) value).get("href");
            String path = parsePathFromRoot(href);
            relPathMap.put(rel, path);
        });
    }

    private String parsePathFromRoot(String href) {
        int slashCount = 0;
        String[] tokens = href.split("");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("/")) {
                slashCount++;
                if (slashCount == 3) {
                    return href.substring(i);
                }
            }
        }
        throw new IllegalStateException("href should have more than 3 /");
    }

}
