package com.github.suloginscene.authserver.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
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
import java.util.Collections;
import java.util.Date;


@Component
@RequiredArgsConstructor
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
            sendForbiddenError(servletResponse);
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

    private void sendForbiddenError(ServletResponse servletResponse) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.sendError(403, "expired jwt");
    }

}
