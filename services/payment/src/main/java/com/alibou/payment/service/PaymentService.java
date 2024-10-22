package com.alibou.payment.service;

import com.alibou.payment.model.PaymentNotificationRequest;
import com.alibou.payment.model.PaymentRequest;
import com.alibou.payment.payment.Payment;
import com.alibou.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;
    public Integer createPayment(PaymentRequest request) {
        // save the payment to the database
        try{
            Payment payment = this.repository.save(mapper.toPayment(request));
        }catch (Exception e){
            System.out.println(e.getMessage()+" 错误洗洗。。。");
        }

        // get instance ready to send it to kafka
        PaymentNotificationRequest paymentNotificationRequest = new PaymentNotificationRequest(
                request.orderReference(),
                request.amount(),
                request.paymentMethod(),
                request.customer().firstName(),
                request.customer().lastName(),
                request.customer().email()
        );
        // send to kafka
        notificationProducer.sendNotification(paymentNotificationRequest);

//        return payment.getId();
        return 0;
    }
}
