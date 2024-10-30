package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		System.out.println("网关服务启动");
		SpringApplication.run(GatewayApplication.class, args);
	}

}
