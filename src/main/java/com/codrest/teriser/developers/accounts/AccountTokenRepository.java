/*
 * AccountTokenRepository.java
 * Author : 박찬형
 * Created Date : 2021-09-03
 */
package com.codrest.teriser.developers.accounts;

import com.codrest.teriser.developers.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTokenRepository extends JpaRepository<AccountToken, Long> {
    Optional<AccountToken> findByEmail(Email email);

    Optional<AccountToken> findByValue(String value);
}
