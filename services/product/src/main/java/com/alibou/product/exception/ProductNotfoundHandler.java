package com.alibou.product.exception;

public class ProductNotfoundHandler extends RuntimeException{

    public ProductNotfoundHandler(String msg){
        super(msg);
    }
}
