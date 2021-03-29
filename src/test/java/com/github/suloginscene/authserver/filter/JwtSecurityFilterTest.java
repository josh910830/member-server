package com.github.suloginscene.authserver.filter;

import com.github.suloginscene.test.TestJwtFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.test.RequestBuilder.ofGet;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("JWT 필터")
@SpringBootTest
@AutoConfigureMockMvc
public class JwtSecurityFilterTest {

    static final String URL = "/";

    @Autowired MockMvc mockMvc;
    @Autowired TestJwtFactory testJwtFactory;

    Long id;

    @BeforeEach
    void setup() {
        id = 1L;
    }


    @Test
    @DisplayName("정상 - 404")
    void jwtFilter_withValidJwt_returns404() throws Exception {
        String validJwt = testJwtFactory.valid(id);

        ResultActions when = mockMvc.perform(
                ofGet(URL).jwt(validJwt).build());

        when.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("만료 - 401")
    void jwtFilter_withExpiredJwt_returns401() throws Exception {
        String expiredJwt = testJwtFactory.expired(id);

        ResultActions when = mockMvc.perform(
                ofGet(URL).jwt(expiredJwt).build());

        when.andExpect(status().isUnauthorized())
                .andExpect(content().string("Expired Jwt"));
    }

    @Test
    @DisplayName("서명 - 401")
    void jwtFilter_withInvalidSignature_returns401() throws Exception {
        String invalidJwt = testJwtFactory.invalid(id);

        ResultActions when = mockMvc.perform(
                ofGet(URL).jwt(invalidJwt).build());

        when.andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid Signature"));
    }

    @Test
    @DisplayName("형식 - 401")
    void jwtFilter_withMalformedJwt_returns401() throws Exception {
        String malformed = testJwtFactory.malformed();

        ResultActions when = mockMvc.perform(
                ofGet(URL).jwt(malformed).build());

        when.andExpect(status().isUnauthorized())
                .andExpect(content().string("Malformed Jwt"));
    }

    @Test
    @DisplayName("없음 - 401")
    void jwtFilter_withoutJwt_returns401() throws Exception {
        ResultActions when = mockMvc.perform(
                ofGet(URL).build());

        when.andExpect(status().isUnauthorized())
                .andExpect(content().string("Jwt Not Exists"));
    }

}
