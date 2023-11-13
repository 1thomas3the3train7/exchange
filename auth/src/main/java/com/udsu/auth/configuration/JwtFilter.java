package com.udsu.auth.configuration;


import com.udsu.auth.model.auth.JwtCredentials;
import com.udsu.auth.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {
    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final ServerHttpRequest serverHttpRequest = exchange.getRequest();
        final String token = getBearerToken(serverHttpRequest);
        if (token != null && jwtUtils.validateAccessToken(token)) {
            final Claims claims = jwtUtils.getAccessClaims(token);
            final JwtCredentials jwtCredentials = jwtUtils.parseClaims(claims);

            if (jwtCredentials.getRoles() != null) {
                jwtCredentials.setAuthenticated(true);
            }
            return ReactiveSecurityContextHolder.getContext().map(m -> {
                m.setAuthentication(jwtCredentials);
                return jwtCredentials;
            }).flatMap(v -> chain.filter(exchange));

        }
        return chain.filter(exchange);
    }

    private String getBearerToken(ServerHttpRequest serverHttpRequest) {
        try {
            final HttpHeaders httpHeaders = serverHttpRequest.getHeaders();
            final String auth = httpHeaders.get("Authorization").get(0);
            System.out.println("AUTH " + auth);
            if (auth.startsWith("Bearer "))
                return auth.substring(7);
            return null;
        } catch (Exception e) {
            log.warn("ERROR PARSE TOKEN, request: {}", serverHttpRequest.getHeaders());
            return null;
        }
    }
}
