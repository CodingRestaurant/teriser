/*
 * LoginResult.java
 * Author : 박찬형
 * Created Date : 2021-08-02
 */
package com.codrest.teriser.developers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@NoArgsConstructor
public class LoginResult {
    private String token;
    private String name;
    private Email email;

    public LoginResult(String token, Developer developer){
        this.token = token;
        name = developer.getName();
        email = developer.getEmail();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("token", token)
                .append("name", name)
                .append("email", email.toString())
                .toString();
    }
}
