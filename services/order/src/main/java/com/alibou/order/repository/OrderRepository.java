package com.alibou.order.repository;

import com.alibou.order.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer > {
}
