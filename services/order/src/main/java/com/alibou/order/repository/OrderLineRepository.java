package com.alibou.order.repository;

import com.alibou.order.model.OrderLineResponse;
import com.alibou.order.order.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    List<OrderLineResponse> findAllByOrderId(Integer orderId);
}
