/*
 * Author: Seokjin Yoon
 * Filename: PaymentItem.java
 * Desc:
 */

package com.codrest.teriser.store.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PaymentPointCard {
    private long id;
    private int quantity;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("quantity", quantity)
                .toString();
    }
}
