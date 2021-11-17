/*
 * AccountTokenService.java
 * Author : 박찬형
 * Created Date : 2021-09-03
 */
package com.codrest.teriser.developers.accounts;

import com.codrest.teriser.developers.Email;
import com.codrest.teriser.errors.ExpiredException;
import com.codrest.teriser.errors.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountTokenService {
    private final AccountTokenRepository accountTokenRepository;

    @Transactional
    public void insert(String name, Email email, AccountMode mode, String tokenValue){
        accountTokenRepository.save(new AccountToken.AccountTokenBuilder()
                .name(name)
                .email(email)
                .mode(mode)
                .value(tokenValue)
                .expiredAt(LocalDateTime.now().plusMinutes(2))
                .build());
    }

    @Transactional(readOnly = true)
    public Optional<AccountToken> findByEmail(Email email){
        return accountTokenRepository.findByEmail(email);
    }

    @Transactional
    public void delete(AccountToken accountToken){
        accountTokenRepository.delete(accountToken);
    }

    @Transactional
    public AccountToken verify(String token){
        AccountToken accountToken = accountTokenRepository.findByValue(token)
                .orElseThrow(() -> new NotFoundException("not found"));

        if(LocalDateTime.now().isAfter(accountToken.getExpiredAt())){
            throw new ExpiredException("token expired. please request again");
        }
        delete(accountToken);

        return accountToken;
    }
}
