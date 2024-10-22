package com.alibou.product.service;

import com.alibou.product.exception.ProductNotfoundHandler;
import com.alibou.product.exception.ProductPurchaseException;
import com.alibou.product.model.*;
import com.alibou.product.product.Category;
import com.alibou.product.product.Product;
import com.alibou.product.repository.CategoryRepository;
import com.alibou.product.repository.ProductRepository;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;
    public Integer createProduct(ProductRequest request) {

        Product product = this.mapper.toProduct(request);

        return this.repository.save(product).getId();
    }
    @Transactional(rollbackFor = ProductPurchaseException.class)
    public List<ProductPurchaseResponse> purchaseProducdts(List<ProductPurchaseRequest> requestList) {
        List<Integer> Ids = requestList.stream().map(ProductPurchaseRequest::productId).toList();

        var storedProducts = this.repository.findAllByIdInOrderById(Ids);
        if(storedProducts.size() != Ids.size()){
            throw new ProductPurchaseException("one or more products not exist");
        }

        var sortedRequest = requestList.stream().sorted(Comparator.comparing(ProductPurchaseRequest::productId)).toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException("Insufficient stock quantity for product with ID:: " + productRequest.productId());
            }
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);
            purchasedProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }
        return purchasedProducts;
    }

    public ProductResponse findById(Integer productId) {
        Optional<Product> product = this.repository.findById(productId);

        if(product.isPresent()){
            Product p = product.get();
            return this.mapper.toProductResponse(p);
        }

        throw new ProductNotfoundHandler("product was not found with Id: " + productId);
    }

    public List<ProductResponse> findAll() {
        List<Product> products = this.repository.findAll();
        List<ProductResponse> list = new ArrayList<>();

        for(Product product : products){
            list.add(this.mapper.toProductResponse(product));
        }

        return list;
    }

    public List<CategoryResponse> findAllCategories() {
        List<Category> categories = this.categoryRepository.findAll();

        List<CategoryResponse> list = new ArrayList<>();

        for(Category c : categories){
            CategoryResponse res = new CategoryResponse(c.getId(), c.getName());
            list.add(res);
        }

        return list;

    }

    public List<ProductResponse> findProductByCategory(Integer categoryId) {
        List<Product> products = this.repository.findAllByCategoryId(categoryId);

        List<ProductResponse> list = new ArrayList<>();

        for(Product product : products){
            list.add(this.mapper.toProductResponse(product));
        }

        return list;
    }
}
