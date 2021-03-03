package com.github.suloginscene.authserver.config;

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

        String jwt = getXAuthToken(servletRequest);

        if (jwt != null && isValid(jwt)) {
            Authentication authentication = toAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getXAuthToken(ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        return httpServletRequest.getHeader("X-AUTH-TOKEN");
    }

    private boolean isValid(String jwtToken) {
        Date now = new Date();
        Date expiration = getExpiration(jwtToken);
        return now.before(expiration);
    }

    private Date getExpiration(String jwtToken) {
        return jwtParser.parseClaimsJws(jwtToken).getBody().getExpiration();
    }

    private Authentication toAuthentication(String token) {
        String audience = getAudience(token);
        return new UsernamePasswordAuthenticationToken(audience, "", Collections.emptySet());
    }

    private String getAudience(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getAudience();
    }

}
