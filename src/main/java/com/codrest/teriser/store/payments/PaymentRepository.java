/*
 * Author: Seokjin Yoon
 * Filename: PaymentRepository.java
 * Desc:
 */

package com.codrest.teriser.store.payments;

import com.codrest.teriser.developers.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByBuyer(Developer buyer);

    Optional<Payment> findByOrderIdAndBuyer(String orderId, Developer buyer);
}
