package com.github.suloginscene.authserver.authserver;

import com.github.suloginscene.authserver.config.ClientProperties;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.api.RequestSupporter;
import com.github.suloginscene.authserver.testing.api.RestDocsConfig;
import com.github.suloginscene.authserver.testing.db.RepositoryProxy;
import com.github.suloginscene.authserver.testing.value.Emails;
import com.github.suloginscene.authserver.testing.value.Passwords;
import com.github.suloginscene.authserver.testing.value.UnknownClientProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs @Import(RestDocsConfig.class)
public class AuthServerTest {

    @Autowired MockMvc mockMvc;
    @Autowired RequestSupporter requestSupporter;
    @Autowired ClientProperties clientProperties;
    @Autowired RepositoryProxy repositoryProxy;

    private static final String URL = "/oauth/token";


    @AfterEach
    void clear() {
        repositoryProxy.clear();
    }


    @Test
    void authServer_withKnownClient_returnsAccessToken() throws Exception {
        repositoryProxy.given(new Member(Emails.VALID, Passwords.ENCODED));

        ResultActions when = mockMvc.perform(
                requestSupporter.postWithClientBasic(URL, clientProperties)
                        .param("username", Emails.VALID.get())
                        .param("password", Passwords.VALID.get())
                        .param("grant_type", "password"));

        ResultActions then = when.andExpect(jsonPath("access_token").exists());

        then.andDo(document("access_token"));
    }

    @Test
    void authServer_withUnknownClient_returns401() throws Exception {
        repositoryProxy.given(new Member(Emails.VALID, Passwords.ENCODED));

        ResultActions when = mockMvc.perform(
                requestSupporter.postWithClientBasic(URL, new UnknownClientProperties())
                        .param("username", Emails.VALID.get())
                        .param("password", Passwords.VALID.get())
                        .param("grant_type", "password"));

        when.andExpect(status().isUnauthorized());
    }

    @Test
    void authServer_withNonExistentUsername_returns400() throws Exception {
        repositoryProxy.given(new Member(Emails.VALID, Passwords.ENCODED));

        ResultActions when = mockMvc.perform(
                requestSupporter.postWithClientBasic(URL, clientProperties)
                        .param("username", Emails.INVALID.get())
                        .param("password", Passwords.VALID.get())
                        .param("grant_type", "password"));

        when.andExpect(status().isBadRequest());
    }

    @Test
    void authServer_withWrongPassword_returns400() throws Exception {
        repositoryProxy.given(new Member(Emails.VALID, Passwords.ENCODED));

        ResultActions when = mockMvc.perform(
                requestSupporter.postWithClientBasic(URL, clientProperties)
                        .param("username", Emails.VALID.get())
                        .param("password", Passwords.INVALID.get())
                        .param("grant_type", "password"));

        when.andExpect(status().isBadRequest());
    }

    @Test
    void authServer_withUnsupportedGrantType_returns400() throws Exception {
        repositoryProxy.given(new Member(Emails.VALID, Passwords.ENCODED));

        ResultActions when = mockMvc.perform(
                requestSupporter.postWithClientBasic(URL, clientProperties)
                        .param("username", Emails.VALID.get())
                        .param("password", Passwords.VALID.get())
                        .param("grant_type", "unsupported"));

        when.andExpect(status().isBadRequest());
    }

}
