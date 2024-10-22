package com.alibou.customer.controller;

import com.alibou.customer.customer.Customer;
import com.alibou.customer.model.CustomerRequest;
import com.alibou.customer.model.CustomerResponse;
import com.alibou.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    public ResponseEntity<Integer> createCustomer(@RequestBody @Valid CustomerRequest request){
        return ResponseEntity.ok(service.createCustomer(request));
    }
    @PutMapping
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CustomerRequest request){
        this.service.updateCustomer(request);
        return ResponseEntity.accepted().build();
    }
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll(){
        return ResponseEntity.ok(this.service.findAllCustomers());
    }
    @GetMapping("/exits/{customer-id}")
    public ResponseEntity<Boolean> existById(@PathVariable("customer-id") Integer customerId){
        return ResponseEntity.ok(this.service.existById(customerId));
    }
    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable("customer-id") Integer customerId){
        return ResponseEntity.ok(this.service.findById(customerId));
    }
    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> delete(@PathVariable("customer-id") Integer customerId){
        this.service.deleteCustomer(customerId);
        return ResponseEntity.accepted().build();
    }
}
