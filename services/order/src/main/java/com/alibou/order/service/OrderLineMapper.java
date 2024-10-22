package com.alibou.order.service;

import com.alibou.order.model.OrderLineRequest;
import com.alibou.order.model.OrderLineResponse;
import com.alibou.order.order.Order;
import com.alibou.order.order.OrderLine;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.id())
                .quantity(request.quantity())
                .order(Order.builder().id(request.orderId()).build())
                .productId(request.ProductId())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLineResponse orderLineResponse) {
        return new OrderLineResponse(orderLineResponse.id(), orderLineResponse.quantity());
    }
}
