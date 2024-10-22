package com.alibou.product.service;

import com.alibou.product.model.ProductPurchaseResponse;
import com.alibou.product.model.ProductRequest;
import com.alibou.product.model.ProductResponse;
import com.alibou.product.product.Category;
import com.alibou.product.product.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public Product toProduct(ProductRequest request) {
        return Product.builder()
                .id(request.Id())
                .name(request.name())
                .description(request.description())
                .availableQuantity(request.availableQuantity())
                .price(request.price())
                .category(Category.builder().id(request.Id()).build())
                .build();

    }

    public ProductResponse toProductResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getAvailableQuantity(),
                p.getPrice(),
                p.getCategory().getId(),
                p.getCategory().getName(),
                p.getCategory().getDescription()
        );
    }

    public ProductPurchaseResponse toProductPurchaseResponse(Product product, double quantity) {
        return new ProductPurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}
