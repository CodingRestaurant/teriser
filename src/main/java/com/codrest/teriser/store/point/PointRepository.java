/*
 * Author: Seokjin Yoon
 * Filename: PointRepository.java
 * Desc:
 */

package com.codrest.teriser.store.point;

import com.codrest.teriser.developers.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findAllByOwner(Developer owner);
}
