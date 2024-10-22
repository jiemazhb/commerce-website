package com.alibou.customer.service;

import com.alibou.customer.Repository.CustomerRepository;
import com.alibou.customer.customer.Customer;
import com.alibou.customer.exception.CustomerNotFoundException;
import com.alibou.customer.model.CustomerRequest;

import com.alibou.customer.model.CustomerResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    public Integer createCustomer(CustomerRequest request) {
        var customer = this.repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        Customer customer = this.repository.findById(request.Id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Cannot update customer:: No customer found with the provided ID:: %s", request.Id())
                ));
        mergerCustomer(customer,request);
        this.repository.save(customer);
    }

    private void mergerCustomer(Customer customer, CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstName())){
            customer.setFirstName(request.firstName());
        }
        if(StringUtils.isNotBlank(request.lastName())){
            customer.setLastName(request.lastName());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }

    }

    public List<CustomerResponse> findAllCustomers() {
//        return this.repository.findAll()
//                .stream().map(this.mapper::fromCustomer)
//                .collect(Collectors.toList());

        List<Customer> customers = this.repository.findAll();

        List<CustomerResponse> customerResponses = new ArrayList<>();

        for (Customer customer : customers) {
            customerResponses.add(this.mapper.fromCustomer(customer));
        }

        return customerResponses;
    }

    public Boolean existById(Integer customerId) {
        return this.repository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(Integer customerId) {
//        return this.repository.findById(customerId)
//                .map(mapper::fromCustomer)
//                .orElseThrow(()-> new CustomerNotFoundException(String.format("No customer found with the provided ID:: %s", customerId)))
        // 从仓库中查找客户
        Optional<Customer> optionalCustomer = this.repository.findById(customerId);

        // 如果客户存在，转换为 CustomerResponse 并返回
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            return this.mapper.fromCustomer(customer);
        }

        // 如果客户不存在，抛出异常
        throw new CustomerNotFoundException(String.format("No customer found with the provided ID:: %s", customerId));
    }

    public void deleteCustomer(Integer customerId) {
        this.repository.deleteById(customerId);
    }
}
