/*
 * Author: Seokjin Yoon
 * Filename: PaymentRequiredException.java
 * Desc:
 */

package com.codrest.teriser.errors;

public class PaymentRequiredException extends RuntimeException {
    public PaymentRequiredException(String message) {
        super(message);
    }

    public PaymentRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
