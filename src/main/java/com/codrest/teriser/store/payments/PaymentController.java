/*
 * Author: Seokjin Yoon
 * Filename: PaymentController.java
 * Desc:
 */

package com.codrest.teriser.store.payments;

import com.codrest.teriser.developers.DeveloperService;
import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.security.JwtAuthentication;
import com.codrest.teriser.store.point.PointCard;
import com.codrest.teriser.store.point.PointCardService;
import com.codrest.teriser.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.codrest.teriser.utils.ApiUtils.success;

@RequestMapping("store/payments")
@RequiredArgsConstructor
@RestController
public class PaymentController {
    private final PaymentService paymentService;
    private final DeveloperService developerService;
    private final PointCardService pointCardService;

    @GetMapping("history")
    public ApiResult<List<PaymentDto>> findAllByBuyer(
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        return success(
                paymentService.findAllByBuyer(
                                developerService.findById(authentication.id)
                                        .orElseThrow(() -> new NotFoundException("Could not found developer for " + authentication.id + "."))
                        ).stream()
                        .map(PaymentDto::new)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("history/{orderId}")
    public ApiResult<PaymentDto> findByOrderIdAndBuyer(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable String orderId
    ) {
        return success(
                paymentService.findByOrderIdAndBuyer(
                                orderId,
                                developerService.findById(authentication.id)
                                        .orElseThrow(() -> new NotFoundException("Could not found developer for " + authentication.id + "."))
                        ).map(PaymentDto::new)
                        .orElseThrow(() -> new NotFoundException("Could not found payment for " + orderId + "."))
        );
    }

    @PostMapping
    public ApiResult<PaymentDto> requestPayment(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @Valid @RequestBody List<PaymentPointCard> request
    ) {
        PointCard[] pointCards = new PointCard[request.size()];
        for (int i = 0; i < request.size(); i++) {
            PaymentPointCard item = request.get(i);
            pointCards[i] = pointCardService.findById(item.getId())
                    .orElseThrow(() -> new NotFoundException("Could not found point card for " + item.getId() + "."));
        }
        return success(
                new PaymentDto(
                        paymentService.requestPayment(
                                request,
                                developerService.findById(authentication.id)
                                        .orElseThrow(() -> new NotFoundException("Could not found developer for " + authentication.id + ".")),
                                pointCards
                        )
                )
        );
    }

    @DeleteMapping("{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResult<Void> cancelPayment(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable String orderId
    ) {
        return success(null);
    }
}
