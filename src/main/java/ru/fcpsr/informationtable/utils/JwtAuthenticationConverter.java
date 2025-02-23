package ru.fcpsr.informationtable.utils;

import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtAuthenticationConverter implements ServerAuthenticationConverter {
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String accessToken = extractTokenFromCookie(exchange.getRequest(), NamingUtil.getInstance().getAccessName());
        String refreshToken = extractTokenFromCookie(exchange.getRequest(), NamingUtil.getInstance().getRefreshName());
        if(accessToken.equals("") && refreshToken.equals("")){
            return Mono.empty();
        }else{
            return Mono.just(new UsernamePasswordAuthenticationToken(accessToken, refreshToken));
        }
    }

    private String extractTokenFromCookie(ServerHttpRequest request, String cookieName) {
        HttpCookie cookie = request.getCookies().getFirst(cookieName);
        return cookie != null ? cookie.getValue() : "";
    }
}
