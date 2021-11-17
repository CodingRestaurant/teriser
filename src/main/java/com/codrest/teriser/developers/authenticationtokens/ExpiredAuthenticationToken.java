/*
 * ExpiredLoginToken.java
 * Author : 박찬형
 * Created Date : 2021-10-03
 */
package com.codrest.teriser.developers.authenticationtokens;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expired_authentication_tokens")
public class ExpiredAuthenticationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "value")
    private String value;
}
