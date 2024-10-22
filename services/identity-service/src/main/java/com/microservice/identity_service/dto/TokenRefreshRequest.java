package com.microservice.identity_service.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class TokenRefreshRequest {
    private String refreshToken;
}
