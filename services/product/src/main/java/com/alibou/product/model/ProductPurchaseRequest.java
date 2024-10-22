package com.alibou.product.model;

import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(
        @NotNull(message = "product is mandatory")
        Integer productId,
        @NotNull(message = "quantiry is mandatory")
        double quantity
) {
}
