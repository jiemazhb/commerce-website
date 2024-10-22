package com.alibou.payment.service;

import com.alibou.payment.model.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {
    private final KafkaTemplate<String, PaymentNotificationRequest> kafkaTemplate;

    public void sendNotification(PaymentNotificationRequest request){
        log.info("sending notification with body <{}>", request);
        Message<PaymentNotificationRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "payment-topic")
                .build();
        // 发送消息并使用异步处理的方式
        CompletableFuture<SendResult<String, PaymentNotificationRequest>> future =
                kafkaTemplate.send(message);

        // 添加回调逻辑
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                // 处理发送失败，将消息发送到死信队列
                System.err.println("Error sending message, sending to DLQ");

                // 构建死信队列的消息
                Message<PaymentNotificationRequest> dlqMessage = MessageBuilder
                        .withPayload(request)
                        .setHeader("topic", "dlq-payment-topic")
                        .build();

                // 发送到死信队列
                kafkaTemplate.send(dlqMessage);
            } else {
                // 发送成功的逻辑
                RecordMetadata metadata = result.getRecordMetadata();
                System.out.println("Message sent successfully with offset: " + metadata.offset());
            }
        });
    }


}
