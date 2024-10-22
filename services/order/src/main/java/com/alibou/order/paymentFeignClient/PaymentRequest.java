package com.alibou.order.paymentFeignClient;

import com.alibou.order.customerFeignClient.CustomerResponse;
import com.alibou.order.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
