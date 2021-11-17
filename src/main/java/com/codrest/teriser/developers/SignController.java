/*
 * SignController.java
 * Author : 박찬형
 * Created Date : 2021-08-28
 */
package com.codrest.teriser.developers;

import com.codrest.teriser.developers.accounts.AccountTokenValueService;
import com.codrest.teriser.developers.authenticationtokens.ExpiredAuthenticationTokenService;
import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.errors.UnauthorizedException;
import com.codrest.teriser.security.Jwt;
import com.codrest.teriser.security.JwtAuthentication;
import com.codrest.teriser.security.JwtAuthenticationToken;
import com.codrest.teriser.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static com.codrest.teriser.security.AuthorityVerifier.verifyAuthority;
import static com.codrest.teriser.utils.ApiUtils.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/developers")
public class SignController {
    private final DeveloperService developerService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final Jwt jwt;
    private final AccountTokenValueService accountTokenValueService;
    private final ExpiredAuthenticationTokenService expiredAuthenticationTokenService;

    @PostMapping("/sign")
    public ResponseEntity<?> login(@Valid @Nullable @RequestBody LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new IllegalArgumentException("login info must be provided");
        }

        Developer developer = developerService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new NotFoundException("Could not found user for " + loginRequest.getEmail().getAddress()));
        if (!developer.isActivate()) {
            throw new UnauthorizedException("unauthorized");
        }

        if (loginRequest.getLoginToken() == null || loginRequest.getLoginToken().isEmpty()) {
            final String loginToken = accountTokenValueService.getTokenValue();
            developerService.updateLoginToken(developer, loginToken);
            emailService.sendLoginToken(developer.getEmail(), loginToken);

            return ResponseEntity.accepted().body(success(true));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new JwtAuthenticationToken(loginRequest.getEmail().getAddress(), loginRequest.getLoginToken()));
            final Developer dev = (Developer) authentication.getDetails();
            final String token = dev.newJwt(jwt, authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toArray(String[]::new));
            developer.setLoginToken(null);
            developer.setLastLoginAt(LocalDateTime.now());
            developerService.update(developer);

            return ResponseEntity.ok(success(new LoginResult(token, dev)));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/sign")
    public ApiResult<Boolean> logout(JwtAuthenticationToken token) {
        verifyAuthority(token, new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN)));

        developerService.findById(((JwtAuthentication) token.getPrincipal()).id)
                .orElseThrow(() -> new NotFoundException("developer not found"));

        expiredAuthenticationTokenService.expireToken(token.getCredentials());

        return success(true);
    }
}
