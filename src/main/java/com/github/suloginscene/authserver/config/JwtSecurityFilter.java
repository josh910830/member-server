package com.github.suloginscene.authserver.config;

import com.github.suloginscene.jjwthelper.InvalidJwtException;
import com.github.suloginscene.jjwthelper.JwtReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtSecurityFilter extends GenericFilterBean {

    private final JwtReader jwtReader;


    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            authenticateByJwt(servletRequest);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (InvalidJwtException e) {
            log.warn(e.getMessage());
            sendForbiddenError(servletResponse, e.getMessage());
        }
    }

    private void authenticateByJwt(ServletRequest servletRequest) throws InvalidJwtException {
        String jwt = getXAuthToken(servletRequest);

        if (jwt != null && !jwt.isBlank()) {
            Authentication authentication = toAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String getXAuthToken(ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        return httpServletRequest.getHeader("X-AUTH-TOKEN");
    }

    private Authentication toAuthentication(String token) throws InvalidJwtException {
        String audience = jwtReader.getAudience(token);
        return new UsernamePasswordAuthenticationToken(audience, "", Collections.emptySet());
    }

    private void sendForbiddenError(ServletResponse servletResponse, String message) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setStatus(403);
        printMessage(httpServletResponse, message);
    }

    private void printMessage(HttpServletResponse httpServletResponse, String message) {
        try (PrintWriter writer = httpServletResponse.getWriter()) {
            writer.print(message);
        } catch (IOException e) {
            log.error("on print http response - {}", e.getMessage());
        }
    }

}
