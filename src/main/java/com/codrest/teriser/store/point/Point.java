/*
 * Author: Seokjin Yoon
 * Filename: Point.java
 * Desc:
 */

package com.codrest.teriser.store.point;

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
@Table(name = "points")
@ToString
public class Point {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @JoinColumn(name = "owner_id")
    @ManyToOne
    private Developer owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal balance;

    @CreatedDate
    private LocalDateTime createAt;
}
