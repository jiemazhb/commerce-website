package com.alibou.notification.entity;

public record Customer(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
