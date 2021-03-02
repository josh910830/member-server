package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.testing.Emails;
import com.github.suloginscene.authserver.testing.Passwords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MemberRestControllerTest {

    @Autowired MockMvc mockMvc;


    @Test
    void signup_onSuccess_returnsLocation() throws Exception {
        mockMvc.perform(
                post("/api/members")
                        .param("email", Emails.VALID.get())
                        .param("password", Passwords.VALID.get())
        )
                .andExpect(status().isCreated());
    }

}