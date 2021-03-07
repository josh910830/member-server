package com.github.suloginscene.authserver.testing.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RequiredArgsConstructor(access = PRIVATE)
public class RequestBuilder {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final MockHttpServletRequestBuilder mockHttpServletRequestBuilder;


    public static RequestBuilder ofPreflight(String url, String origin, HttpMethod requestMethod, Object... requestHeaders) {
        return new RequestBuilder(options(url))
                .attachOrigin(origin)
                .attachHeader(ACCESS_CONTROL_REQUEST_METHOD, requestMethod)
                .attachHeader(ACCESS_CONTROL_REQUEST_HEADERS, requestHeaders);
    }

    public static RequestBuilder ofPost(String url) {
        return new RequestBuilder(post(url));
    }

    public static RequestBuilder ofGet(String url) {
        return new RequestBuilder(get(url));
    }

    public RequestBuilder attachJson(Object object) throws JsonProcessingException {
        String json = OBJECT_MAPPER.writeValueAsString(object);
        return new RequestBuilder(mockHttpServletRequestBuilder.contentType(APPLICATION_JSON).content(json));
    }

    public RequestBuilder attachJwt(String jwt) {
        return new RequestBuilder(mockHttpServletRequestBuilder.header("X-AUTH-TOKEN", jwt));
    }

    public RequestBuilder attachOrigin(String origin) {
        return new RequestBuilder(mockHttpServletRequestBuilder.header(ORIGIN, origin));
    }

    public RequestBuilder attachHeader(String name, Object... value) {
        if (value.length == 0) {
            return this;
        }
        return new RequestBuilder(mockHttpServletRequestBuilder.header(name, value));
    }

    public MockHttpServletRequestBuilder build() {
        return mockHttpServletRequestBuilder;
    }

}
