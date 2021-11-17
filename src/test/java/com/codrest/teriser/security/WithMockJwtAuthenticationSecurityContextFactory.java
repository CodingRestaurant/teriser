/*
 * WithMockJwtAuthenticationSecurityContextFactory.java
 * Author : 박찬형
 * Created Date : 2021-08-12
 */
package com.codrest.teriser.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

public class WithMockJwtAuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtAuthentication> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtAuthentication annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        JwtAuthenticationToken authentication =
                new JwtAuthenticationToken(
                        new JwtAuthentication(annotation.id(), annotation.name()),
                        annotation.password(),
                        createAuthorityList(annotation.role())
                );
        context.setAuthentication(authentication);
        return context;
    }

}
