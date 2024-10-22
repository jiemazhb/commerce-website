package com.alibou.order.service;

import com.alibou.order.model.OrderLineRequest;
import com.alibou.order.model.OrderLineResponse;
import com.alibou.order.order.OrderLine;
import com.alibou.order.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;
    public Integer saveOrderLine(OrderLineRequest request) {
        OrderLine order = mapper.toOrderLine(request);
        return this.repository.save(order).getId();
    }

    public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
        return this.repository.findAllByOrderId(orderId).stream()
                .map(mapper::toOrderLineResponse).collect(Collectors.toList());
    }
}
