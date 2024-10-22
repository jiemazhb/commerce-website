package com.alibou.order.controller;

import com.alibou.order.model.OrderLineResponse;
import com.alibou.order.service.OrderLineService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class OrderLineController {
    private final OrderLineService service;

    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderLineResponse>> findByOrderId(@PathVariable(name = "order-id") Integer orderId){
        return ResponseEntity.ok(this.service.findAllByOrderId(orderId));
    }

}
