/*
 * UnAuthorizedException.java
 * Author : 박찬형
 * Created Date : 2021-07-31
 */
package com.codrest.teriser.errors;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

}
