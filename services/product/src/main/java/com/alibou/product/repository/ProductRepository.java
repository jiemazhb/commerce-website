package com.alibou.product.repository;

import com.alibou.product.model.ProductResponse;
import com.alibou.product.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByIdInOrderById(List<Integer> ids);

    List<Product> findAllByCategoryId(Integer categoryId);

}
