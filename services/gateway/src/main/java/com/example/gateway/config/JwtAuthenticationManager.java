package com.example.gateway.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final WebClient webClient;

    public JwtAuthenticationManager(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9898").build(); // 替换为你的 identity service 地址
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        System.out.println("调用后端验证token");
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/identity/validate")
                        .queryParam("token", token)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    if ("Token is valid".equals(response)) {
                        System.out.println("token好用");
                        return new UsernamePasswordAuthenticationToken(token, token, Collections.emptyList());
                    }
                    throw new RuntimeException("Invalid token");
                });
    }
}
