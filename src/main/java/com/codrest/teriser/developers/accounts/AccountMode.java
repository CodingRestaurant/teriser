/*
 * AccountMode.java
 * Author : 박찬형
 * Created Date : 2021-09-03
 */
package com.codrest.teriser.developers.accounts;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AccountMode {
    REGISTER("REGISTER", "가입"),
    DEACTIVATE("DEACTIVATE", "탈퇴");

    final String modeName;
    final String mailText;

    public String value(){
        return modeName;
    }

    public String mailText(){
        return mailText;
    }
}
