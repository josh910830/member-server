package com.github.suloginscene.authserver.filter;

import com.github.suloginscene.authserver.member.api.MemberRestController;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.config.TestJwtFactoryConfig;
import com.github.suloginscene.authserver.testing.db.RepositoryFacade;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
import com.github.suloginscene.jjwthelper.TestJwtFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.suloginscene.authserver.testing.api.RequestBuilder.ofGet;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestJwtFactoryConfig.class)
@DisplayName("JWT 필터")
public class JwtSecurityFilterTest {

    static final String URL = linkTo(MemberRestController.class).toString();

    @Autowired MockMvc mockMvc;
    @Autowired TestJwtFactory testJwtFactory;
    @Autowired RepositoryFacade repositoryFacade;

    Member member;


    @BeforeEach
    void setup() {
        member = DefaultMembers.create();
    }

    @AfterEach
    void clear() {
        repositoryFacade.clear();
    }


    @Test
    @DisplayName("정상 - 200")
    void getMember_onSuccess_returns200() throws Exception {
        repositoryFacade.given(member);
        String jwt = testJwtFactory.valid(member.getId());

        ResultActions when = mockMvc.perform(
                ofGet(URL + "/" + member.getId()).jwt(jwt).build());

        when.andExpect(status().isOk());
    }

    @Test
    @DisplayName("만료 - 403")
    void getMember_withExpiredJwt_returns403() throws Exception {
        repositoryFacade.given(member);
        String jwt = testJwtFactory.expired(member.getId());

        ResultActions when = mockMvc.perform(
                ofGet(URL + "/" + member.getId()).jwt(jwt).build());

        when.andExpect(status().isForbidden())
                .andExpect(content().string("ExpiredJwtException"));
    }

    @Test
    @DisplayName("서명 - 403")
    void getMember_withInvalidSignature_returns403() throws Exception {
        repositoryFacade.given(member);
        String jwt = testJwtFactory.invalid(member.getId());

        ResultActions when = mockMvc.perform(
                ofGet(URL + "/" + member.getId()).jwt(jwt).build());

        when.andExpect(status().isForbidden())
                .andExpect(content().string("SignatureException"));
    }

    @Test
    @DisplayName("형식 - 403")
    void getMember_withMalformedJwt_returns403() throws Exception {
        repositoryFacade.given(member);
        String jwt = testJwtFactory.malformed();

        ResultActions when = mockMvc.perform(
                ofGet(URL + "/" + member.getId()).jwt(jwt).build());

        when.andExpect(status().isForbidden())
                .andExpect(content().string("MalformedJwtException"));
    }

}
