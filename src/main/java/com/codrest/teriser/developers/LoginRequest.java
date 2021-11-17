/*
 * LoginRequest.java
 * Author : 박찬형
 * Created Date : 2021-08-02
 */
package com.codrest.teriser.developers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotNull(message = "email must be provided")
    private Email email;
    @Size(max = 132, message = "login token length must be less than 132")
    private String loginToken;
}
