package com.alibou.order.kafka;

import com.alibou.order.customerFeignClient.CustomerResponse;
import com.alibou.order.customerFeignClient.PurchaseResponse;
import com.alibou.order.order.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
