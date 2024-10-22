package com.alibou.product.model;

import java.math.BigDecimal;

public record ProductPurchaseResponse (
        Integer Id,
        String name,
        String description,
        BigDecimal price,
        double quantity
){
}
