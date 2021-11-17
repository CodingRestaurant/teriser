/*
 * Author: Seokjin Yoon
 * Filename: PointRequest.java
 * Desc:
 */

package com.codrest.teriser.store.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PointRequest {
    @NotBlank(message = "receiptId must be provided.")
    private String receiptId;

    @NotBlank(message = "orderId must be provided.")
    private String orderId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("receiptId", receiptId)
                .append("orderId", orderId)
                .toString();
    }
}
