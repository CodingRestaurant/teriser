/*
 * Author: Seokjin Yoon
 * Filename: ProductDto.java
 * Desc:
 */

package com.codrest.teriser.store.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

import static org.springframework.beans.BeanUtils.copyProperties;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class ProductDto {
    private Long seq;
    private String name;
    private BigDecimal price;

    public ProductDto(Product source) {
        copyProperties(source, this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("name", name)
                .append("price", price)
                .toString();
    }
}
