/*
 * AccountTokenValueService.java
 * Author : 박찬형
 * Created Date : 2021-09-13
 */
package com.codrest.teriser.developers.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;


import static com.codrest.teriser.utils.SecureTokenGenerator.generateToken;

@Service
@RequiredArgsConstructor
public class AccountTokenValueService {
    private static final int TOKEN_POOL_SIZE = 10000;
    private final AccountTokenValueRepository accountTokenValueRepository;

    @PostConstruct
    @Transactional
    public void init() {
        long refillCount = TOKEN_POOL_SIZE - accountTokenValueRepository.countByUsedIsFalse();
        for(long i = 0; i < refillCount; i++){
            insert();
        }
    }

    @Async
    @Transactional
    public void insert() {
        String value;
        do {
            value = generateToken();
        }while (accountTokenValueRepository.existsByValue(value));
        accountTokenValueRepository.save(AccountTokenValue.builder()
                .value(value)
                .build());
    }

    @Transactional
    public String getTokenValue() {
        Long nextSeq = accountTokenValueRepository.findNextSeq();
        AccountTokenValue accountTokenValue;
        if(nextSeq == null){
            accountTokenValue = AccountTokenValue.builder()
                    .value(generateToken())
                    .build();
        }
        else{
            accountTokenValue = accountTokenValueRepository.findById(nextSeq).get();
        }

        accountTokenValue.setUsed(true);
        accountTokenValueRepository.save(accountTokenValue);

        return accountTokenValue.getValue();
    }
}
