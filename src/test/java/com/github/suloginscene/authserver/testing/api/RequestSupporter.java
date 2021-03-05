package com.github.suloginscene.authserver.testing.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Component
@RequiredArgsConstructor
public class RequestSupporter {

    private final ObjectMapper objectMapper;


    public MockHttpServletRequestBuilder optionsFromCrossOrigin(String url, String origin) {
        return options(url)
                .header(ORIGIN, origin)
                .header(ACCESS_CONTROL_REQUEST_HEADERS, CONTENT_TYPE)
                .header(ACCESS_CONTROL_REQUEST_METHOD, POST);
    }

    public MockHttpServletRequestBuilder postWithJson(String url, Object body) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(body);
        return post(url).contentType(APPLICATION_JSON).content(json);
    }

    public MockHttpServletRequestBuilder getWithJwt(String url, String jwt) {
        return get(url).header("X-AUTH-TOKEN", jwt);
    }

}
