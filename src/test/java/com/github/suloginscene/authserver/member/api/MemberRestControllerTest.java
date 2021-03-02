package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.testing.Emails;
import com.github.suloginscene.authserver.testing.Passwords;
import com.github.suloginscene.authserver.testing.RepositoryProxy;
import com.github.suloginscene.authserver.testing.RequestSupporter;
import com.github.suloginscene.authserver.testing.RestDocsConfig;
import org.junit.jupiter.api.AfterEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs @Import(RestDocsConfig.class)
class MemberRestControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired RequestSupporter requestSupporter;
    @Autowired RepositoryProxy repositoryProxy;

    private static final String URL = linkTo(MemberRestController.class).toString();


    @AfterEach
    void clear() {
        repositoryProxy.clear();
    }


    @Test
    void signup_onSuccess_returns201() throws Exception {
        SignupRequest request = new SignupRequest(Emails.VALID.get(), Passwords.VALID.get());
        ResultActions when = mockMvc.perform(requestSupporter.postWithJson(URL, request));

        ResultActions then = when.andExpect(status().isCreated());

        then.andDo(document("signup"));
    }

    @Test
    void signup_withNullEmail_returns400() throws Exception {
        SignupRequest request = new SignupRequest(null, Passwords.VALID.get());
        ResultActions when = mockMvc.perform(requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

    @Test
    void signup_withNullPassword_returns400() throws Exception {
        SignupRequest request = new SignupRequest(Emails.VALID.get(), null);
        ResultActions when = mockMvc.perform(requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

    @Test
    void signup_withInvalidEmail_returns400() throws Exception {
        SignupRequest request = new SignupRequest(Emails.INVALID.get(), Passwords.VALID.get());
        ResultActions when = mockMvc.perform(requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

    @Test
    void signup_withInvalidPassword_returns400() throws Exception {
        SignupRequest request = new SignupRequest(Emails.VALID.get(), Passwords.INVALID.get());
        ResultActions when = mockMvc.perform(requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

}
