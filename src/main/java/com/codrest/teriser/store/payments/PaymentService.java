/*
 * Author: Seokjin Yoon
 * Filename: PaymentService.java
 * Desc:
 */

package com.codrest.teriser.store.payments;

import com.codrest.teriser.developers.Developer;
import com.codrest.teriser.errors.ForbiddenException;
import com.codrest.teriser.errors.InternalServerErrorException;
import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.errors.PaymentRequiredException;
import com.codrest.teriser.store.point.PointCard;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@ConfigurationProperties(prefix = "pgmodule")
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Value("${pgmodule.app-id}")
    private String apiKey;
    @Value("${pgmodule.secret-key}")
    private String apiSecret;

    @Transactional(readOnly = true)
    public List<Payment> findAllByBuyer(Developer buyer) {
        checkNotNull(buyer, "buyer must be provided.");

        return paymentRepository.findAllByBuyer(buyer);
    }

    @Transactional(readOnly = true)
    public Optional<Payment> findByOrderIdAndBuyer(String orderId, Developer buyer) {
        checkNotNull(orderId, "orderId must be provided.");
        checkNotNull(buyer, "buyer must be provided.");

        return paymentRepository.findByOrderIdAndBuyer(orderId, buyer);
    }

    @Transactional
    public Payment requestPayment(List<PaymentPointCard> paymentPointCards, Developer buyer, PointCard[] pointCards) {
        String name = pointCards[0].getName();
        if (pointCards.length > 1) {
            name += " 외 " + (pointCards.length - 1);
        }

        BigDecimal point = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ZERO;
        for (int i = 0; i < paymentPointCards.size(); i++) {
            point = point.add(pointCards[i].getPoint().multiply(BigDecimal.valueOf(paymentPointCards.get(i).getQuantity())));
            amount = amount.add(pointCards[i].getPrice().multiply(BigDecimal.valueOf(paymentPointCards.get(i).getQuantity())));
        }

        Payment payment = new Payment();
        payment.setBuyer(buyer);
        payment.setOrderId(buyer.getName() + "_" + Objects.hash(buyer, Arrays.hashCode(pointCards), System.currentTimeMillis()));
        payment.setName(name);
        payment.setPoint(point);
        payment.setAmount(amount);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment verifyPayment(Payment payment, Developer buyer) {
        checkNotNull(payment, "payment must be provided.");
        checkNotNull(buyer, "buyer must be provided.");

        if (!payment.getBuyer().equals(buyer)) {
            throw new NotFoundException("Could not found payment for " + buyer.getName() + ".");
        }

        IamportClient iamportClient = new IamportClient(apiKey, apiSecret);
        try {
            IamportResponse<com.siot.IamportRestClient.response.Payment> paymentResponse = iamportClient.paymentByImpUid(payment.getReceiptId());
            if (Objects.nonNull(paymentResponse.getResponse())) {
                com.siot.IamportRestClient.response.Payment paymentData = paymentResponse.getResponse();
                if (payment.getReceiptId().equals(paymentData.getImpUid()) && payment.getOrderId().equals(paymentData.getMerchantUid()) && payment.getAmount().compareTo(paymentData.getAmount()) == 0) {
                    PaymentMethod method = PaymentMethod.valueOf(paymentData.getPayMethod().toUpperCase());
                    PaymentStatus status = PaymentStatus.valueOf(paymentData.getStatus().toUpperCase());
                    payment.setMethod(method);
                    payment.setStatus(status);
                    paymentRepository.save(payment);
                    if (status.equals(PaymentStatus.READY)) {
                        if (method.equals(PaymentMethod.VBANK)) {
                            throw new PaymentRequiredException(paymentData.getVbankNum() + " " + paymentData.getVbankDate() + " " + paymentData.getVbankName());
                        } else {
                            throw new PaymentRequiredException("Payment was not completed.");
                        }
                    } else if (status.equals(PaymentStatus.PAID)) {
                        payment.setPaidAt(paymentData.getPaidAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                        paymentRepository.save(payment);
                    } else if (status.equals(PaymentStatus.FAILED)) {
                        throw new ForbiddenException("Payment failed.");
                    } else if (status.equals(PaymentStatus.CANCELLED)) {
                        throw new ForbiddenException("This is a cancelled payment.");
                    }
                } else {
                    throw new ForbiddenException("The amount paid and the amount to be paid do not match.");
                }
            } else {
                throw new NotFoundException("Could not found payment for " + payment.getReceiptId() + ".");
            }
        } catch (IamportResponseException e) {
            e.printStackTrace();
            switch (e.getHttpStatusCode()) {
                case 401 -> throw new InternalServerErrorException("Authentication token not passed or invalid.");
                case 404 -> throw new NotFoundException("Could not found payment for " + payment.getReceiptId() + ".");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return payment;
    }

    @Transactional
    public Payment verifyPayment(String receiptId, String orderId, Developer buyer) {
        checkNotNull(receiptId, "receiptId must be provided.");

        Optional<Payment> optionalPayment = findByOrderIdAndBuyer(orderId, buyer);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setReceiptId(receiptId);
            return verifyPayment(payment, buyer);
        } else {
            throw new NotFoundException("Could not found payment for " + orderId + ".");
        }
    }
}
