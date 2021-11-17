/*
 * Author: Seokjin Yoon
 * Filename: PurchaseRequest.java
 * Desc:
 */

package com.codrest.teriser.store.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PurchaseRequest {
    private long itemId;
    private int quantity;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("itemId", itemId)
                .append("quantity", quantity)
                .toString();
    }
}
