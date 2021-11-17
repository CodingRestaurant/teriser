/*
 * ExpiredLoginTokenService.java
 * Author : 박찬형
 * Created Date : 2021-10-03
 */
package com.codrest.teriser.developers.authenticationtokens;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpiredAuthenticationTokenService {
    private final ExpiredAuthenticationTokenRepository expiredAuthenticationTokenRepository;

    @Transactional
    public void expireToken(String token){
        expiredAuthenticationTokenRepository.save(ExpiredAuthenticationToken.builder().value(token).build());
    }

    @Transactional(readOnly = true)
    public boolean existsTokenByValue(String value){
        return expiredAuthenticationTokenRepository.existsByValue(value);
    }
}
