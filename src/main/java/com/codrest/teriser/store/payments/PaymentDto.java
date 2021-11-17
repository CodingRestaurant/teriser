/*
 * Author: Seokjin Yoon
 * Filename: PaymentDto.java
 * Desc:
 */

package com.codrest.teriser.store.payments;

import com.codrest.teriser.developers.Developer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class PaymentDto {
    private Long seq;
    private Developer buyer;
    private String receiptId;
    private String orderId;
    private PaymentMethod method;
    private String name;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createAt;
    private LocalDateTime paidAt;
    private LocalDateTime failedAt;
    private BigDecimal cancelledAmount;
    private LocalDateTime cancelledAt;

    public PaymentDto(Payment source) {
        copyProperties(source, this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("buyer", buyer.getName())
                .append("orderId", orderId)
                .append("method", method.toString())
                .append("name", name)
                .append("amount", amount)
                .append("status", status.toString().toLowerCase())
                .append("createAt", createAt)
                .append("paidAt", paidAt)
                .append("failedAt", failedAt)
                .append("cancelledAmount", cancelledAmount)
                .append("cancelledAt", cancelledAt)
                .toString();
    }
}
