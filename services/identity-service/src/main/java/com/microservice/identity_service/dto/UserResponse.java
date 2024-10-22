package com.microservice.identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private int id;
    private String email;
    private String password;
    private String username;
}
