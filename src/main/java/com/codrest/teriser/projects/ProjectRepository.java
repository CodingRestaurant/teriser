/*
 * Author: Seokjin Yoon, Hyeokwoo Kwon
 * Filename: ProjectRepository.java
 * Desc:
 */

package com.codrest.teriser.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);
    Optional<Project> findByClientToken(String token);
}
