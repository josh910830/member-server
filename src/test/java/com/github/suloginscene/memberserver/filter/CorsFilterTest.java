package com.github.suloginscene.memberserver.filter;

import com.github.suloginscene.jwt.JwtFactory;
import com.github.suloginscene.property.SecurityProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.test.RequestBuilder.ofGet;
import static com.github.suloginscene.test.RequestBuilder.ofPost;
import static com.github.suloginscene.test.RequestBuilder.ofPreflight;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("CORS 필터")
@SpringBootTest
@AutoConfigureMockMvc
public class CorsFilterTest {

    static final String URL = "/404";

    @Autowired MockMvc mockMvc;
    @Autowired JwtFactory jwtFactory;
    @Autowired SecurityProperties securityProperties;

    String validOrigin;
    String invalidOrigin;

    String jwt;


    @BeforeEach
    void setup() {
        jwt = jwtFactory.create(1L);

        validOrigin = securityProperties.getOrigins().split(",")[0];
        invalidOrigin = "https://invalid.com";
    }


    @Test
    @DisplayName("OPTIONS 허용 - 200")
    void options_fromValidOrigin_returns200() throws Exception {
        ResultActions when = mockMvc.perform(
                ofPreflight(URL, validOrigin, POST).build());

        when.andExpect(status().isOk());
    }

    @Test
    @DisplayName("OPTIONS 비허용 - 403")
    void options_fromInvalidOrigin_returns403() throws Exception {
        ResultActions when = mockMvc.perform(
                ofPreflight(URL, invalidOrigin, POST).build());

        when.andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("POST 허용 - 404")
    void post_fromValidOrigin_returns404() throws Exception {
        ResultActions when = mockMvc.perform(
                ofPost(URL).jwt(jwt).origin(validOrigin).build());

        when.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST 비허용 - 403")
    void post_fromInvalidOrigin_returns403() throws Exception {
        ResultActions when = mockMvc.perform(
                ofPost(URL).jwt(jwt).origin(invalidOrigin).build());

        when.andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("GET 허용 - 404")
    void get_fromValidOrigin_returns404() throws Exception {
        ResultActions when = mockMvc.perform(
                ofGet(URL).jwt(jwt).origin(validOrigin).build());

        when.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET 비허용 - 403")
    void get_fromInvalidOrigin_returns403() throws Exception {
        ResultActions when = mockMvc.perform(
                ofGet(URL).jwt(jwt).origin(invalidOrigin).build());

        when.andExpect(status().isForbidden());
    }

}
