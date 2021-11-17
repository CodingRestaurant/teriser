/*
 * AlreadyExistException.java
 * Author : 박찬형
 * Created Date : 2021-08-18
 */
package com.codrest.teriser.errors;

public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException(String message){
        super(message);
    }

    public AlreadyExistException(String message, Throwable cause){
        super(message, cause);
    }
}
