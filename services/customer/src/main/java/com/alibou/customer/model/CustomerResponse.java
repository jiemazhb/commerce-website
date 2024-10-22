package com.alibou.customer.model;

import com.alibou.customer.customer.Address;
import jakarta.persistence.criteria.CriteriaBuilder;

public record CustomerResponse(
        Integer id,
        String firstName,
        String lastName,
        String email,
        Address address
) {
}
