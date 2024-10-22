package com.alibou.customer.model;

import com.alibou.customer.customer.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        Integer Id,
        @NotNull(message = "Customer firstname is required")
        String firstName,
        @NotNull(message = "customer lastname is required")
        String lastName,
        @NotNull(message = "customer email is required")
        @Email(message = "customer email is not a valid email address")
        String email,
        Address address
) { }
