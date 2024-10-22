package com.alibou.order.order;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
 @AllArgsConstructor
 @NoArgsConstructor
 @Entity
 @Builder
 @Getter
 @Setter
 @EntityListeners(AuditingEntityListener.class)
 @Table(name = "customerOrder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String reference;
    private BigDecimal totalAmount;
    @Enumerated(STRING)
    private PaymentMethod paymentMethod;
    private Integer customerId;
    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLine;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
