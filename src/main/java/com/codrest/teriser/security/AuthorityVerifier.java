/*
 * AuthorityVerifier.java
 * Author : 박찬형
 * Created Date : 2021-08-16
 */
package com.codrest.teriser.security;

import com.codrest.teriser.developers.Role;
import com.codrest.teriser.errors.UnauthorizedException;

import java.util.Set;

import static org.springframework.security.core.authority.AuthorityUtils.authorityListToSet;

public class AuthorityVerifier {
    public static void verifyAuthority(JwtAuthenticationToken token, Set<Role> allowedAuthority){
        if(token == null){
            throw new UnauthorizedException("unauthorized");
        }

        Set<String> authorities = authorityListToSet(token.getAuthorities());
        for(Role authority : allowedAuthority){
            if(authorities.contains(authority.value())){
                return;
            }
        }

        throw new UnauthorizedException("unauthorized");
    }
}
