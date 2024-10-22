package com.alibou.payment.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
    String id,
    @NotNull(message = "Firstname is required")
    String firstName,
    @NotNull(message = "Last name is required")
    String lastName,
    @NotNull(message = "Email is required")
    @Email(message = "the email is not correct formatted")
    String email
) {
}
