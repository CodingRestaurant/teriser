/*
 * DeveloperRepository.java
 * Author : 박찬형
 * Created Date : 2021-08-01
 */
package com.codrest.teriser.developers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    boolean existsByEmailAndActivateIsTrue(Email email);

    Optional<Developer> findByEmail(Email email);
}
