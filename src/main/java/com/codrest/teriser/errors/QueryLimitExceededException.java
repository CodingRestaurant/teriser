/*
 * LimitExceedException.java
 * Author : 박찬형
 * Created Date : 2021-08-16
 */
package com.codrest.teriser.errors;

public class QueryLimitExceededException extends RuntimeException{
    public QueryLimitExceededException(String message){
        super(message);
    }

    public QueryLimitExceededException(String message, Throwable cause){
        super(message, cause);
    }
}
