package com.khanh.antimessenger.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.khanh.antimessenger.constant.TokenType;
import com.khanh.antimessenger.utilities.AccessTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

import static com.khanh.antimessenger.constant.SecurityConstant.OPTIONS_HTTP_METHOD;
import static com.khanh.antimessenger.constant.SecurityConstant.TOKEN_PREFIX;

@Component
//@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final AccessTokenService accessTokenService;
    private final HandlerExceptionResolver resolver;

    public JwtAuthorizationFilter(AccessTokenService accessTokenService, @Qualifier("errorAttributes") HandlerExceptionResolver resolver) {
        this.accessTokenService = accessTokenService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
                filterChain.doFilter(request,response);
                return;
            }
            try {
                String token = authHeader.substring(TOKEN_PREFIX.length());
                String username = accessTokenService.getSubject(token);
                if (accessTokenService.isTokenValid(username, token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<GrantedAuthority> authorities = accessTokenService.getAuthorities(token);
                    Authentication authentication =
                            accessTokenService.getAuthentication(username, authorities, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } catch (JWTVerificationException e) {
                resolver.resolveException(request, response, null, e);
            }
        }
        filterChain.doFilter(request,response);
    }
}
