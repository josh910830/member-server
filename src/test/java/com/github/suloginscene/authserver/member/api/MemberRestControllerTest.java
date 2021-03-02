package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.testing.Emails;
import com.github.suloginscene.authserver.testing.Passwords;
import com.github.suloginscene.authserver.testing.RequestSupporter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MemberRestControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired RequestSupporter requestSupporter;

    private static final String URL = linkTo(MemberRestController.class).toString();


    @Test
    void signup_onSuccess_returnsLocation() throws Exception {
        SignupRequest request = new SignupRequest(Emails.VALID.get(), Passwords.VALID.get());
        ResultActions when = mockMvc.perform(requestSupporter.postWithJson(URL, request));

        ResultActions then = when.andExpect(status().isCreated());
    }

}
