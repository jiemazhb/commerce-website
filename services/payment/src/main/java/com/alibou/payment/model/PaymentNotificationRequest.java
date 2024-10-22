package com.alibou.payment.model;

import com.alibou.payment.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotificationRequest(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String firstname,
        String lastname,
        String email
) {
}
