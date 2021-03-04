package com.github.suloginscene.authserver.global;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.AccessDeniedException;


public class AuthorizationException extends AccessDeniedException {

    public AuthorizationException(Long audience, WebMvcLinkBuilder resource) {
        super(audience + " has no authorization to access " + resource);
    }

}
