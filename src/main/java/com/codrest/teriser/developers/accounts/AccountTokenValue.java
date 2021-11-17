/*
 * AccountTokenValue.java
 * Author : 박찬형
 * Created Date : 2021-09-10
 */
package com.codrest.teriser.developers.accounts;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "account_token_values")
public class AccountTokenValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "value")
    private String value;

    @Column(name = "used")
    private boolean used;
}
