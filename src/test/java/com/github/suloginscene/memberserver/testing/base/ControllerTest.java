package com.github.suloginscene.memberserver.testing.base;

import com.github.suloginscene.jwt.JwtFactory;
import com.github.suloginscene.property.SecurityProperties;
import com.github.suloginscene.test.RestDocsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
@Slf4j
public abstract class ControllerTest extends IntegrationTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected JwtFactory jwtFactory;
    @Autowired protected SecurityProperties securityProperties;

}
