package com.github.suloginscene.memberserver;

import com.github.suloginscene.jwt.JwtFactory;
import com.github.suloginscene.mail.MailMessage;
import com.github.suloginscene.mail.Mailer;
import com.github.suloginscene.memberserver.jwt.api.JwtRequest;
import com.github.suloginscene.memberserver.member.api.request.MemberOnForgetPasswordRequest;
import com.github.suloginscene.memberserver.member.api.request.MemberPasswordChangeRequest;
import com.github.suloginscene.memberserver.member.api.request.MemberSignupRequest;
import com.github.suloginscene.memberserver.member.api.request.MemberVerificationRequest;
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
 * ????????? ????????? ???????????? ?????? ???????????? ??????????????????.
 * ????????? ????????? ???????????? ???????????????, ????????? ??????????????? ??????????????? ???????????? ????????????.
 * ?????? ???????????? ????????? ?????? ???????????? ????????? ????????? ???????????? ????????????.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("?????? ???????????? ?????????")
public class AcceptanceScenarioTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtFactory jwtFactory;
    @SpyBean Mailer mailer;

    String email = "test@email.com";
    String password = "password";


    // ???????????????????????? _links ????????? ???????????????.
    Map<String, String> relPathMap = new HashMap<>();

    // ????????? ?????? ?????? ?????? ??????????????????.
    ThreadLocal<MailMessage> tempMail = new ThreadLocal<>();

    // void mailer.send()??? ??????????????? ???????????? ?????? ???????????????.
    Answer<Void> proxiedVoid = invocation -> {
        MailMessage mail = invocation.getArgument(0);
        tempMail.set(mail);
        invocation.callRealMethod();
        return null;
    };

    // ?????? ?????? ??? ???????????? ?????????????????? ??????????????? ???????????????.
    String jwt;


    @Order(1)
    @Test
    @DisplayName("????????? - 200: ?????? 4???")
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
    @DisplayName("?????? ?????? - 200: ?????? ?????? ??????")
    void signup() throws Exception {
        doAnswer(proxiedVoid).when(mailer).send(any());

        String url = relPathMap.get("signup");

        MemberSignupRequest request = new MemberSignupRequest(email, password);
        ResultActions signup = mockMvc.perform(ofPost(url).json(request).build());

        MvcResult result = signup.andExpect(status().isOk()).andReturn();

        setRelPathMap(result);
        assertThat(relPathMap.get("verify")).startsWith("/api/members/verify/");

        then(mailer).should().send(any());
        assertThat(tempMail.get().getRecipient()).isEqualTo(email);
        assertThat(tempMail.get().getTitle()).isEqualTo("[Scene] ???????????? ?????? ??????");
        assertThat(tempMail.get().getContent()).startsWith("???????????? ?????? ??????: ");
    }

    @Order(3)
    @Test
    @DisplayName("?????? ?????? - 201")
    void verify() throws Exception {
        String url = relPathMap.get("verify");

        String token = tempMail.get().getContent().split(": ")[1];
        MemberVerificationRequest request = new MemberVerificationRequest(token);
        ResultActions verify = mockMvc.perform(ofPost(url).json(request).build());

        verify.andExpect(status().isCreated());
    }

    @Order(4)
    @Test
    @DisplayName("???????????? ?????? - 200: ?????? ??????")
    void onForgetPassword() throws Exception {
        doAnswer(proxiedVoid).when(mailer).send(any());

        String url = relPathMap.get("onForgetPassword");

        MemberOnForgetPasswordRequest request = new MemberOnForgetPasswordRequest(email);
        ResultActions onForgetPassword = mockMvc.perform(ofPost(url).json(request).build());

        onForgetPassword.andExpect(status().isOk());

        then(mailer).should().send(any());
        assertThat(tempMail.get().getRecipient()).isEqualTo(email);
        assertThat(tempMail.get().getTitle()).isEqualTo("[Scene] ?????? ???????????? ??????");
        assertThat(tempMail.get().getContent()).startsWith("??? ????????????: ");

        password = tempMail.get().getContent().split(": ")[1];
    }


    @Order(5)
    @Test
    @DisplayName("JWT ?????? - 200")
    void issueJwt() throws Exception {
        String url = relPathMap.get("issueJwt");

        JwtRequest request = new JwtRequest(email, password);
        ResultActions issueJwt = mockMvc.perform(ofPost(url).json(request).build());

        MvcResult result = issueJwt.andExpect(status().isOk()).andReturn();

        Map<String, Object> jsonMap = toResponseAsJsonMap(result);
        String accessToken = jsonMap.get("access_token").toString();
        String refreshToken = jsonMap.get("refresh_token").toString();
        assertThat(accessToken).matches(".+\\..+\\..+");
        assertThat(refreshToken).isNotNull();

        jwt = accessToken;
    }


    @Order(6)
    @Test
    @DisplayName("??? ?????? - 200")
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
    @DisplayName("???????????? ?????? - 200")
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
    @DisplayName("?????? - 204")
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
