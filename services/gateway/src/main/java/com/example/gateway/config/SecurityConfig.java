package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtAuthenticationManager authenticationManager, JwtSecurityContextRepository securityContextRepository) {

        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource())) // 配置 CORS
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/orders/**").authenticated()
                        .anyExchange().permitAll()
                )
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("https://jiemazhb.github.io");
        config.addAllowedOrigin("https://danny-portfolio.ngrok.app");
        config.addAllowedMethod("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
