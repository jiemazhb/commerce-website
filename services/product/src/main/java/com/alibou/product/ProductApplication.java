package com.alibou.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductApplication {

	public static void main(String[] args) {
		System.out.println("产品类启动");
		SpringApplication.run(ProductApplication.class, args);
	}

}
