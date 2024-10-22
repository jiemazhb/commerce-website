package com.alibou.product.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest (
        Integer Id,
        @NotNull(message = "Product name is required")
        String name,
        @NotNull(message = "Product description is required")
        String description,
        @Positive(message = "available quantity should be positive")
        double availableQuantity,
        @Positive(message = "price should be positive")
        BigDecimal price,
        @NotNull(message = "category id is required")
        Integer categoryId

) {
}
