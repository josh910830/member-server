package com.github.suloginscene.authserver.member.api;

import org.springframework.security.access.AccessDeniedException;


public class ResourceAccessDeniedException extends AccessDeniedException {

    public ResourceAccessDeniedException(String username, String resourceType, Long resourceId) {
        super(username + " can not access to " + resourceType + " " + resourceId);
    }

}
