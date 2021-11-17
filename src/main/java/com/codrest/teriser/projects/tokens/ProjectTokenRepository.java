/*
 * Author : Hyeokwoo Kwon
 * Filename : ProjectTokenRepository.java
 * Desc :
 */

package com.codrest.teriser.projects.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectTokenRepository extends JpaRepository<ProjectToken, Long> {
    Optional<ProjectToken> findByToken(String token);

    @Query("select min(seq) from ProjectToken where issuedDate is null")
    Long findMinIdUnissuedToken();

    boolean existsByToken(String token);
}
