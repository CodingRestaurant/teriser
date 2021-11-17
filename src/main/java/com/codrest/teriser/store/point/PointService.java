/*
 * Author: Seokjin Yoon
 * Filename: PointService.java
 * Desc:
 */

package com.codrest.teriser.store.point;

import com.codrest.teriser.developers.Developer;
import com.codrest.teriser.errors.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@RequiredArgsConstructor
@Service
public class PointService {
    private final PointRepository pointRepository;

    @Transactional
    public Point transactPoint(Developer owner, String name, BigDecimal amount) {
        checkNotNull(owner, "owner must be provided.");
        checkNotNull(name, "name must be provided.");
        checkNotNull(amount, "amount must be provided.");

        List<Point> points = pointRepository.findAllByOwner(owner);
        BigDecimal balance = points.size() > 0 ? points.get(points.size() - 1).getBalance() : BigDecimal.ZERO;

        BigDecimal result = balance.add(amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new ForbiddenException("Could not transact point.");
        }

        Point point = new Point();
        point.setOwner(owner);
        point.setName(name);
        point.setAmount(amount);
        point.setBalance(result);

        return pointRepository.save(point);
    }
}
