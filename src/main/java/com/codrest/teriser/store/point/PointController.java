/*
 * Author: Seokjin Yoon
 * Filename: PointService.java
 * Desc:
 */

package com.codrest.teriser.store.point;

import com.codrest.teriser.developers.Developer;
import com.codrest.teriser.developers.DeveloperService;
import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.security.JwtAuthentication;
import com.codrest.teriser.store.payments.Payment;
import com.codrest.teriser.store.payments.PaymentService;
import com.codrest.teriser.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.codrest.teriser.utils.ApiUtils.success;

@RequestMapping("store/point")
@RequiredArgsConstructor
@RestController
public class PointController {
    private final PointService pointService;
    private final PointCardService pointCardService;
    private final DeveloperService developerService;
    private final PaymentService paymentService;

    @GetMapping
    public ApiResult<List<PointCard>> findAll() {
        return success(pointCardService.findAll());
    }

    @PutMapping
    public ApiResult<Point> chargePoint(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @Valid @RequestBody PointRequest request
    ) {
        Developer developer = developerService.findById(authentication.id)
                .orElseThrow(() -> new NotFoundException("Could not found developer for " + authentication.id + "."));
        Payment payment = paymentService.verifyPayment(request.getReceiptId(), request.getOrderId(), developer);
        return success(pointService.transactPoint(developer, payment.getName(), payment.getAmount()));
    }
}
