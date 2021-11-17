/*
 * Author : Hyeokwoo Kwon
 * Filename : BadRequestException.java
 * Desc :
 */
package com.codrest.teriser.errors;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message){
        super(message);
    }
}
