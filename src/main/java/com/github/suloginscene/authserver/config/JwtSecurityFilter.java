package com.github.suloginscene.authserver.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
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
import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtSecurityFilter extends GenericFilterBean {

    private final JwtParser jwtParser;


    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            authenticateByJwt(servletRequest);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (JwtException e) {
            log.warn(e.getClass().getSimpleName());
            sendForbiddenError(servletResponse, e);
        }
    }

    private void authenticateByJwt(ServletRequest servletRequest) {
        String jwt = getXAuthToken(servletRequest);

        if (jwt != null && isValid(jwt)) {
            Authentication authentication = toAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String getXAuthToken(ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        return httpServletRequest.getHeader("X-AUTH-TOKEN");
    }

    private boolean isValid(String jwtToken) {
        Date now = new Date();
        Date expiration = jwtParser.parseClaimsJws(jwtToken).getBody().getExpiration();
        return now.before(expiration);
    }

    private Authentication toAuthentication(String token) {
        String audience = jwtParser.parseClaimsJws(token).getBody().getAudience();
        return new UsernamePasswordAuthenticationToken(audience, "", Collections.emptySet());
    }

    private void sendForbiddenError(ServletResponse servletResponse, JwtException e) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setStatus(403);
        printMessage(httpServletResponse, e.getClass().getSimpleName());
    }

    private void printMessage(HttpServletResponse httpServletResponse, String message) {
        try (PrintWriter writer = httpServletResponse.getWriter()) {
            writer.print(message);
        } catch (IOException e) {
            log.error("on print http response - {}", e.getMessage());
        }
    }

}
