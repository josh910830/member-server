package com.github.suloginscene.authserver.testing.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.suloginscene.authserver.config.ClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Component
@RequiredArgsConstructor
public class RequestSupporter {

    private final ObjectMapper objectMapper;


    public MockHttpServletRequestBuilder postWithJson(String url, Object body) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(body);
        return post(url).contentType(APPLICATION_JSON).content(json);
    }

    public MockHttpServletRequestBuilder postWithClientBasic(String url, ClientProperties clientProperties) {
        RequestPostProcessor clientBasic = httpBasic(clientProperties.getId(), clientProperties.getSecret());
        return post(url).with(clientBasic);
    }

}
