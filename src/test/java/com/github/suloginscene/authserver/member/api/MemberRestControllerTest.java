package com.github.suloginscene.authserver.member.api;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs @Import(RestDocsConfig.class)
class MemberRestControllerTest {

    static final String URL = linkTo(MemberRestController.class).toString();

    @Autowired MockMvc mockMvc;
    @Autowired RequestSupporter requestSupporter;
    @Autowired RepositoryProxy repositoryProxy;

    String email;
    String password;


    @BeforeEach
    void setup() {
        email = DefaultMembers.EMAIL;
        password = DefaultMembers.RAW_PASSWORD;
    }

    @AfterEach
    void clear() {
        repositoryProxy.clear();
    }


    @Test
    void signup_onSuccess_returns201() throws Exception {
        SignupRequest request = new SignupRequest(email, password);
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        ResultActions then = when.andExpect(status().isCreated());

        then.andDo(document("signup"));
    }

    @Test
    void signup_withNullEmail_returns400() throws Exception {
        SignupRequest request = new SignupRequest(null, password);
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

    @Test
    void signup_withNullPassword_returns400() throws Exception {
        SignupRequest request = new SignupRequest(email, null);
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

    @Test
    void signup_withInvalidEmail_returns400() throws Exception {
        SignupRequest request = new SignupRequest("notEmail", password);
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

    @Test
    void signup_withInvalidPassword_returns400() throws Exception {
        SignupRequest request = new SignupRequest(email, "short");
        ResultActions when = mockMvc.perform(
                requestSupporter.postWithJson(URL, request));

        when.andExpect(status().isBadRequest());
    }

    // TODO signup_onDuplicate_returns400()

}
