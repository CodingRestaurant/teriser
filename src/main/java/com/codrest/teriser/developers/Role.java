/*
 * Author : 나상혁 : Kasania
 * Filename : Role
 * Desc :
 */
package com.codrest.teriser.developers;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN")
    ;

    final String role;
    Role(String role) {
        this.role = role;
    }

    public String value() {
        return role;
    }
}
