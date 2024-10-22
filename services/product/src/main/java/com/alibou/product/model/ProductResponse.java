package com.alibou.product.model;

import java.math.BigDecimal;

public record ProductResponse (
        Integer Id,
        String name,
        String description,
        double availableQuantity,
        BigDecimal price,
        Integer categoryId,
        String categoryName,
        String categoryDescription
) {
}
