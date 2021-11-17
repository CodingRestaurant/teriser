/*
 * Author: Seokjin Yoon
 * Filename: PointCardRepository.java
 * Desc:
 */

package com.codrest.teriser.store.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointCardRepository extends JpaRepository<PointCard, Long> {
    List<PointCard> findAllByAvailableTrue();
}
