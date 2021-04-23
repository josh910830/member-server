package com.github.suloginscene.memberserver.root.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.suloginscene.test.RequestBuilder.ofGet;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("루트 정적 컨트롤러")
class RootForwardControllerTest {

    @Autowired MockMvc mockMvc;


    @Test
    @DisplayName("홈")
    void home() throws Exception {
        mockMvc.perform(ofGet("/").build())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("오류")
    void error() throws Exception {
        mockMvc.perform(ofGet("/error").build())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("파비콘")
    void favicon() throws Exception {
        mockMvc.perform(ofGet("/favicon.ico").build())
                .andExpect(status().isOk());
    }

}
