/*
 * AccountToken.java
 * Author : 박찬형
 * Created Date : 2021-09-03
 */
package com.codrest.teriser.developers.accounts;

import com.codrest.teriser.developers.Email;
import com.codrest.teriser.developers.EmailConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_tokens")
public class AccountToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Column(name = "token_value")
    private String value;

    @Column(name = "account_mode")
    private AccountMode mode;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
}
