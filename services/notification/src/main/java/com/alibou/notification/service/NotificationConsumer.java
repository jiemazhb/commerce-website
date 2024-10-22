package com.alibou.notification.service;

import com.alibou.notification.entity.Notification;
import com.alibou.notification.entity.NotificationType;
import com.alibou.notification.entity.OrderConfirmation;
import com.alibou.notification.entity.PaymentConfirmation;
import com.alibou.notification.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository repository;
    private final EmailService emailService;

    private final KafkaTemplate<String, OrderConfirmation> kafkaOrderTemplate;
    private final KafkaTemplate<String, PaymentConfirmation> kafkaPaymentTemplate;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotifications(PaymentConfirmation paymentConfirmation) throws MessagingException {
        try{
            log.info(String.format("consuming the message from order-topic topic::%s", paymentConfirmation));

            Notification notification = Notification.builder()
                    .type(NotificationType.PAYMENT_CONFIRMATION)
                    .notificationDate(LocalDateTime.now())
                    .paymentConfirmation(paymentConfirmation)
                    .build();

            repository.save(notification);

            String fullName = paymentConfirmation.firstname()+" "+paymentConfirmation.lastname();

            emailService.sentPaymentSuccessEmail(paymentConfirmation.email(),fullName, paymentConfirmation.amount(),paymentConfirmation.orderReference());
        }catch (Exception e){
            // 捕获所有异常并处理
            log.error("Error processing payment confirmation: " + paymentConfirmation, e);

            // 将消息发送到死信队列
            kafkaPaymentTemplate.send("dlq-payment-topic", paymentConfirmation);
        }

    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationNotification(OrderConfirmation orderConfirmation) {
        try {
            log.info(String.format("consuming the message from order-topic::%s", orderConfirmation));

            Notification notification = Notification.builder()
                    .type(NotificationType.ORDER_CONFIRMATION)
                    .notificationDate(LocalDateTime.now())
                    .orderConfirmation(orderConfirmation)
                    .build();

            repository.save(notification);

            String fullName = orderConfirmation.customer().firstName() + " " + orderConfirmation.customer().lastName();

            emailService.sentOrderConfirmationEmail(orderConfirmation.customer().email(),
                    fullName,
                    orderConfirmation.totalAmount(),
                    orderConfirmation.orderReference(),
                    orderConfirmation.products());

        } catch (Exception e) {
            // 捕获所有异常并处理
            log.error("Error processing order confirmation: " + orderConfirmation, e);

            // 将消息发送到死信队列
            kafkaOrderTemplate.send("dlq-order-topic", orderConfirmation);
        }
    }

}
