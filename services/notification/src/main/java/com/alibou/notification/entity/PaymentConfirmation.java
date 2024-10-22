package com.alibou.notification.entity;

import java.math.BigDecimal;

public record PaymentConfirmation(
        String orderReference,
        BigDecimal amount,

        PaymentMethod paymentMethod,
        String firstname,
        String lastname,
        String email
) {
}
