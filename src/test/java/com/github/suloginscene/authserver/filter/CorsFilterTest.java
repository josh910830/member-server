package com.github.suloginscene.authserver.filter;

import com.github.suloginscene.authserver.config.JwtProperties;
import com.github.suloginscene.authserver.member.api.MemberRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.authserver.testing.api.RequestBuilder.ofPreflight;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("CORS 필터")
public class CorsFilterTest {

    static final String URL = linkTo(MemberRestController.class).toString();

    @Autowired MockMvc mockMvc;
    @Autowired JwtProperties jwtProperties;


    @Test
    @DisplayName("OPTIONS 성공 - 200")
    void options_fromValidOrigin_returns200() throws Exception {
        String validOrigin = jwtProperties.getUrls().split(",")[0];
        ResultActions when = mockMvc.perform(
                ofPreflight(URL, validOrigin, POST, CONTENT_TYPE, ACCEPT).build());

        when.andExpect(status().isOk());
    }

    @Test
    @DisplayName("OPTIONS 실패 - 403")
    void options_fromInvalidOrigin_returns403() throws Exception {
        String invalidOrigin = "http://invalid.com";
        ResultActions when = mockMvc.perform(
                ofPreflight(URL, invalidOrigin, POST).build());

        when.andExpect(status().isForbidden());
    }


}
