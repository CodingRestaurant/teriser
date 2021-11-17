/*
 * Author: Seokjin Yoon
 * Filename: Payment.java
 * Desc:
 */

package com.codrest.teriser.store.payments;

import com.codrest.teriser.developers.Developer;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Setter
@Table(name = "payments", indexes = @Index(name = "index_payments_order_id", columnList = "orderId"))
@ToString
public class Payment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @JoinColumn(name = "buyer_id")
    @ManyToOne
    private Developer buyer;

    @Column(nullable = false, unique = true)
    private String receiptId;

    @Column(nullable = false, unique = true)
    private String orderId;

    private PaymentMethod method;

    private String name;

    @Column(nullable = false)
    private BigDecimal point;

    @Column(nullable = false)
    private BigDecimal amount;

    @Builder.Default
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.READY;

    @CreatedDate
    private LocalDateTime createAt;

    private LocalDateTime paidAt;

    private LocalDateTime failedAt;

    @Builder.Default
    private BigDecimal cancelledAmount = BigDecimal.ZERO;

    private LocalDateTime cancelledAt;
}
