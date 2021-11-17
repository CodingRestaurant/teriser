/*
 * Author: Seokjin Yoon
 * Filename: InternalServerErrorException.java
 * Desc:
 */

package com.codrest.teriser.errors;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
