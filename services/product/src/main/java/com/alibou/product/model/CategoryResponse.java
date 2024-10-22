package com.alibou.product.model;

import com.alibou.product.product.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import java.util.List;

public record CategoryResponse (
        Integer id,
        String name
){
}
