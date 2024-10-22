package com.alibou.order.model;

public record OrderLineRequest(Integer id, Integer orderId, Integer ProductId, double quantity) {
}
