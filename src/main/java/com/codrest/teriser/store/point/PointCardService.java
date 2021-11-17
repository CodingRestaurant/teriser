/*
 * Author: Seokjin Yoon
 * Filename: PointCardService.java
 * Desc:
 */

package com.codrest.teriser.store.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@RequiredArgsConstructor
@Service
public class PointCardService {
    private final PointCardRepository pointCardRepository;

    @Transactional(readOnly = true)
    public List<PointCard> findAll() {
        return pointCardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PointCard> findById(Long pointCardId) {
        checkNotNull(pointCardId, "pointCardId must be provided.");

        return pointCardRepository.findById(pointCardId);
    }
}
