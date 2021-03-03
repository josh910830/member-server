package com.github.suloginscene.authserver.authserver;

import com.github.suloginscene.authserver.config.ClientProperties;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.api.RequestSupporter;
import com.github.suloginscene.authserver.testing.api.RestDocsConfig;
import com.github.suloginscene.authserver.testing.db.RepositoryProxy;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    static final String URL = "/oauth/token";

    @Autowired MockMvc mockMvc;
    @Autowired RequestSupporter requestSupporter;
    @Autowired ClientProperties clientProperties;
    @Autowired RepositoryProxy repositoryProxy;

    Member member;
    String email;
    String password;


    @BeforeEach
    void setup() {
        member = DefaultMembers.create();
        email = DefaultMembers.EMAIL;
        password = DefaultMembers.RAW_PASSWORD;
    }

    @AfterEach
    void clear() {
        repositoryProxy.clear();
    }


    @Test
    void authServer_withKnownClient_returnsAccessToken() throws Exception {
        repositoryProxy.given(member);

        ResultActions when = mockMvc.perform(
                requestSupporter.postWithClientBasic(URL, clientProperties)
                        .param("username", email)
                        .param("password", password)
                        .param("grant_type", "password"));

        ResultActions then = when.andExpect(jsonPath("access_token").exists());

        then.andDo(document("access_token"));
    }

    @Test
    void authServer_withUnknownClient_returns401() throws Exception {
        repositoryProxy.given(member);

        ClientProperties unknownClientProperties = createUnknownClientProperties();
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithClientBasic(URL, unknownClientProperties)
                        .param("username", email)
                        .param("password", password)
                        .param("grant_type", "password"));

        when.andExpect(status().isUnauthorized());
    }

    private ClientProperties createUnknownClientProperties() {
        ClientProperties clientProperties = new ClientProperties();
        clientProperties.setId("unknown");
        clientProperties.setSecret("unknown");
        return clientProperties;
    }

    @Test
    void authServer_withNonExistentUsername_returns400() throws Exception {
        repositoryProxy.given(member);

        ResultActions when = mockMvc.perform(
                requestSupporter.postWithClientBasic(URL, clientProperties)
                        .param("username", "nonExistent@email.com")
                        .param("password", password)
                        .param("grant_type", "password"));

        when.andExpect(status().isBadRequest());
    }

    @Test
    void authServer_withWrongPassword_returns400() throws Exception {
        repositoryProxy.given(member);

        ResultActions when = mockMvc.perform(
                requestSupporter.postWithClientBasic(URL, clientProperties)
                        .param("username", email)
                        .param("password", "wrongPassword")
                        .param("grant_type", "password"));

        when.andExpect(status().isBadRequest());
    }

    @Test
    void authServer_withUnsupportedGrantType_returns400() throws Exception {
        repositoryProxy.given(member);

        ResultActions when = mockMvc.perform(
                requestSupporter.postWithClientBasic(URL, clientProperties)
                        .param("username", email)
                        .param("password", password)
                        .param("grant_type", "unsupported"));

        when.andExpect(status().isBadRequest());
    }

}
