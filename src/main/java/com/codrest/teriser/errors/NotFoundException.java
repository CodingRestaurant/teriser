/*
 * NotFoundException.java
 * Author : 박찬형
 * Created Date : 2021-07-31
 */
package com.codrest.teriser.errors;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
