/*
 * RegisterRequest.java
 * Author : 박찬형
 * Created Date : 2021-08-21
 */
package com.codrest.teriser.developers;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull(message = "name must be provided")
    @NotBlank(message = "name must be not blank")
    @Size(max = 12, message = "name length must be less than 12")
    private String name;
    @NotNull(message = "email must be provided")
    private String email;
}
