package com.alibou.order.model;

import com.alibou.order.order.PaymentMethod;

import java.math.BigDecimal;

public record OrderResponse(
        Integer id,
        String reference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer customerId
) {
}
