/*
 * WithMockJwtAuthentication.java
 * Author : 박찬형
 * Created Date : 2021-08-12
 */
package com.codrest.teriser.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationSecurityContextFactory.class)
public @interface WithMockJwtAuthentication {

    long id() default 1L;

    String name() default "tester";

    String role() default "USER";

    String password() default "1234";
}
