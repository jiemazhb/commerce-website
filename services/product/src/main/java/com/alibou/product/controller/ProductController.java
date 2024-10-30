package com.alibou.product.controller;

import com.alibou.product.model.*;
import com.alibou.product.product.Category;
import com.alibou.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> findAllCategories(){
        return ResponseEntity.ok(this.service.findAllCategories());
    }
    @GetMapping("/category")
    public ResponseEntity<List<ProductResponse>> findProductByCategory(@RequestParam(value = "selectedCategory", required = false) Integer categoryId){
        return ResponseEntity.ok(this.service.findProductByCategory(categoryId));
    }
    @PostMapping
    public ResponseEntity<Integer> createProduct(@RequestBody @Valid ProductRequest request){
        return ResponseEntity.ok(this.service.createProduct(request));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(@RequestBody List<ProductPurchaseRequest> requestList){
        return ResponseEntity.ok(this.service.purchaseProducdts(requestList));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable("product-id") Integer productId){
        return ResponseEntity.ok(this.service.findById(productId));
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(){
        System.out.println("获得产品信息");
        return ResponseEntity.ok(this.service.findAll());
    }
}
