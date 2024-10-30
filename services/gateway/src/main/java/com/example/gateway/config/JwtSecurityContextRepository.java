package com.example.gateway.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtAuthenticationManager authenticationManager;

    public JwtSecurityContextRepository(JwtAuthenticationManager authenticationManager) {
        System.out.println("JwtSecurityContextRepository <==方法在==》 JwtSecurityContextRepository class");
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        System.out.println("为什么");
        String token = extractTokenFromRequest(exchange.getRequest());
        if (token != null) {
            System.out.println("为什么333");
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, token))
                    .map(SecurityContextImpl::new);
        }
        System.out.println("为什么2222");
        return Mono.empty();
    }

    private String extractTokenFromRequest(ServerHttpRequest request) {
        System.out.println("我是谁");
        // 从请求头中提取 JWT
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println("我是谁1111");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            System.out.println("我是谁222");
            return bearerToken.substring(7);
        }
        System.out.println("我是谁3333");
        return null;
    }
}
