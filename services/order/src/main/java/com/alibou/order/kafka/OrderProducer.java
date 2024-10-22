package com.alibou.order.kafka;

//import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Service;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    public void sendOrderConfirmation(OrderConfirmation orderConfirmation) {
        // 构建消息
        Message<OrderConfirmation> message = MessageBuilder
                .withPayload(orderConfirmation)
                .setHeader(KafkaHeaders.TOPIC, "order-topic")
                .build();

        // 发送消息并使用异步处理的方式
        CompletableFuture<SendResult<String, OrderConfirmation>> future =
                kafkaTemplate.send(message);

        // 添加回调逻辑
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                // 处理发送失败，将消息发送到死信队列
                System.err.println("Error sending message, sending to DLQ");

                // 构建死信队列的消息
                Message<OrderConfirmation> dlqMessage = MessageBuilder
                        .withPayload(orderConfirmation)
                        .setHeader("topic", "dlq-order-topic")
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


