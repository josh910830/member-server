package com.github.suloginscene.authserver.root.api;

import com.github.suloginscene.authserver.testing.base.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.test.RequestBuilder.ofGet;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("루트 API")
class RootRestControllerTest extends ControllerTest {

    static final String URL = linkTo(RootRestController.class).toString();


    @Test
    @DisplayName("인덱스")
    void getIndex() throws Exception {
        ResultActions when = mockMvc.perform(
                ofGet(URL).build());

        ResultActions then = when.andExpect(status().isOk());

        then.andDo(document("index"));
    }

}
