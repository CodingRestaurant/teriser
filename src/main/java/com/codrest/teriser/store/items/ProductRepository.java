/*
 * Author: Seokjin Yoon
 * Filename: ProductRepository.java
 * Desc:
 */

package com.codrest.teriser.store.items;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
