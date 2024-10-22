package com.alibou.customer.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.ControllerAdvice;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerNotFoundException extends RuntimeException {
    private final String msg;
}
