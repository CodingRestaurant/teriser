/*
 * Author: Seokjin Yoon
 * Filename: GoneException.java
 * Desc:
 */

package com.codrest.teriser.errors;

public class GoneException extends RuntimeException {
    public GoneException(String message) {
        super(message);
    }

    public GoneException(String message, Throwable cause) {
        super(message, cause);
    }
}
