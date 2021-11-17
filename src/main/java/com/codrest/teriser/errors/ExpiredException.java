/*
 * ExpiredException.java
 * Author : 박찬형
 * Created Date : 2021-08-16
 */
package com.codrest.teriser.errors;

public class ExpiredException extends RuntimeException{
    public ExpiredException(String message){
        super(message);
    }

    public ExpiredException(String message, Throwable cause){
        super(message, cause);
    }
}
