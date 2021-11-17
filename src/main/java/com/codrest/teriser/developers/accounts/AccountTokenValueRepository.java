/*
 * AccountTokenValueRepository.java
 * Author : 박찬형
 * Created Date : 2021-09-13
 */
package com.codrest.teriser.developers.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTokenValueRepository extends JpaRepository<AccountTokenValue, Long> {
    @Query("select min(seq) from AccountTokenValue where used = false")
    Long findNextSeq();

    long countByUsedIsFalse();

    boolean existsByValue(String value);
}
