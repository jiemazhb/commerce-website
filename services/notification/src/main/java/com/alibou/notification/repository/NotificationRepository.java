package com.alibou.notification.repository;

import com.alibou.notification.entity.Notification;
import com.alibou.notification.entity.PaymentConfirmation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

}
