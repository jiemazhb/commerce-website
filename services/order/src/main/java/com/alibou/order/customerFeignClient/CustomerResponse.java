package com.alibou.order.customerFeignClient;

public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
