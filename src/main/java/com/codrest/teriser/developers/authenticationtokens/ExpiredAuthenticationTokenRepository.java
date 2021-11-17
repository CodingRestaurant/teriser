/*
 * ExpiredLoginTokenRepository.java
 * Author : 박찬형
 * Created Date : 2021-10-03
 */
package com.codrest.teriser.developers.authenticationtokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpiredAuthenticationTokenRepository extends JpaRepository<ExpiredAuthenticationToken, Long> {
    boolean existsByValue(String value);
}
