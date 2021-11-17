/*
 * Author: Seokjin Yoon
 * Filename: PointCard.java
 * Desc:
 */

package com.codrest.teriser.store.point;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "point_cards")
@ToString
public class PointCard {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal point;

    @Column(nullable = false)
    private BigDecimal price;

    @Builder.Default
    @Column(nullable = false)
    private boolean available = true;
}
