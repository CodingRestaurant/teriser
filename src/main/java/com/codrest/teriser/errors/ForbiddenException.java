/*
 * Author: Seokjin Yoon
 * Filename: ForbiddenException.java
 * Desc:
 */

package com.codrest.teriser.errors;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
