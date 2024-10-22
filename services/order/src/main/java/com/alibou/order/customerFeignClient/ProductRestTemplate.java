package com.alibou.order.customerFeignClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import com.alibou.order.exception.BussinessException;
import com.alibou.order.model.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRestTemplate {
    @Value("${application.config.product-url}")
    private String productUrl;
    private final RestTemplate restTemplate;

    public List<PurchaseResponse> purchaseProduct(List<PurchaseRequest> requests){

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(requests,headers);
        ParameterizedTypeReference<List<PurchaseResponse>> responseType = new ParameterizedTypeReference<>() {};

        ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(productUrl + "/purchase",
                HttpMethod.POST,
                requestEntity,
                responseType);

        if(responseEntity.getStatusCode().isError()){
            throw new BussinessException("an error occured while processing the product pruchase" + responseEntity.getStatusCode());
        }
        return responseEntity.getBody();
    }

}
