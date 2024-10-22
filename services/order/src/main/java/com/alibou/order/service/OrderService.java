package com.alibou.order.service;

import com.alibou.order.kafka.OrderConfirmation;
import com.alibou.order.kafka.OrderProducer;
import com.alibou.order.exception.BussinessException;
import com.alibou.order.customerFeignClient.CustomerClient;
import com.alibou.order.customerFeignClient.CustomerResponse;
import com.alibou.order.customerFeignClient.ProductRestTemplate;
import com.alibou.order.customerFeignClient.PurchaseResponse;
import com.alibou.order.model.OrderLineRequest;
import com.alibou.order.model.OrderRequest;
import com.alibou.order.model.OrderResponse;
import com.alibou.order.model.PurchaseRequest;
import com.alibou.order.order.Order;
import com.alibou.order.paymentFeignClient.PaymentClient;
import com.alibou.order.paymentFeignClient.PaymentRequest;
import com.alibou.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductRestTemplate productRestTemplate;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;
    @Transactional
    public Integer createOrder(OrderRequest request) {
        // check if customer exist
        CustomerResponse customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BussinessException("no customer was found with customerId: " + request.customerId()));

        // get products
        List<PurchaseResponse> purchaseProducts = this.productRestTemplate.purchaseProduct(request.products());

        // confirm current order
        Order order = this.repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()){
            this.orderLineService.saveOrderLine(
                    new OrderLineRequest(null, order.getId(), purchaseRequest.productId(), purchaseRequest.quantity())
            );
        }
        // make payment
        PaymentRequest paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );

        paymentClient.requestOrderPayment(paymentRequest);

        // send confirmation through Kafka
        this.orderProducer.sendOrderConfirmation(new OrderConfirmation(
                request.reference(),
                request.amount(),
                request.paymentMethod(),
                customer,
                purchaseProducts
        ));

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return this.repository.findAll()
                .stream().map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return this.repository.findById(orderId).map(mapper::fromOrder)
                .orElseThrow(()->new EntityNotFoundException("No order found with given id: "+ orderId));
    }
}
