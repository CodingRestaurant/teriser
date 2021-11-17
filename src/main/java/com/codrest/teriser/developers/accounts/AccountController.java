/*
 * AccountController.java
 * Author : 박찬형
 * Created Date : 2021-08-28
 */
package com.codrest.teriser.developers.accounts;

import com.codrest.teriser.developers.*;
import com.codrest.teriser.developers.authenticationtokens.ExpiredAuthenticationTokenService;
import com.codrest.teriser.errors.AlreadyExistException;
import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.errors.UnauthorizedException;
import com.codrest.teriser.security.JwtAuthentication;
import com.codrest.teriser.security.JwtAuthenticationToken;
import com.codrest.teriser.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/developers/account")
public class AccountController {
    private final DeveloperService developerService;
    private final EmailService emailService;
    private final AccountTokenService accountTokenService;
    private final AccountTokenValueService accountTokenValueService;
    private final ExpiredAuthenticationTokenService expiredAuthenticationTokenService;

    @GetMapping("/{token}")
    public ResponseEntity<?> verify(@PathVariable(name = "token") String token) {
        if (token.isEmpty() || token.length() > 132) {
            throw new UnauthorizedException("unauthorized");
        }
        AccountToken accountToken = accountTokenService.verify(token);

        Developer developer = developerService.findByEmail(accountToken.getEmail())
                .orElseThrow(() -> new NotFoundException("developer not found"));
        if (accountToken.getMode() == AccountMode.REGISTER) {
            developer.setActivate(true);
            developerService.update(developer);

            return new ResponseEntity<>(success(true), HttpStatus.CREATED);
        } else {
            developer.setActivate(false);
            developerService.update(developer);

            return ResponseEntity.ok(success(true));
        }
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<Boolean> register(@Valid @Nullable @RequestBody RegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new IllegalArgumentException("developer info must be provided");
        }

        Email email = Email.of(registerRequest.getEmail());
        if (developerService.isActivatedDeveloper(email)) {
            throw new AlreadyExistException("email " + email.getAddress() + " already exist");
        }

        AccountToken accountToken = accountTokenService.findByEmail(email).orElse(null);
        if(accountToken == null){
            developerService.insert(Developer.builder()
                    .name(registerRequest.getName())
                    .email(Email.of(registerRequest.getEmail()))
                    .activate(false)
                    .build());
        }
        else if(accountToken.getExpiredAt().isAfter(LocalDateTime.now())){
            throw new AlreadyExistException("email " + email.getAddress() + " already requested register");
        }
        else{
            accountTokenService.delete(accountToken);
        }

        String registerToken = accountTokenValueService.getTokenValue();
        accountTokenService.insert(registerRequest.getName(), email, AccountMode.REGISTER, registerToken);

        emailService.sendAccountVerifyEmail(email, registerToken, AccountMode.REGISTER);

        return success(true);
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<Boolean> deactivate(JwtAuthenticationToken token) {
        verifyAuthority(token, new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN)));

        Developer developer = developerService.findById(((JwtAuthentication) token.getPrincipal()).id)
                .orElseThrow(() -> new NotFoundException("developer not found"));
        AccountToken accountToken = accountTokenService.findByEmail(developer.getEmail()).orElse(null);
        if(accountToken == null){
            expiredAuthenticationTokenService.expireToken(token.getCredentials());
        }
        else if(accountToken.getExpiredAt().isAfter(LocalDateTime.now())){
            throw new AlreadyExistException("email " + developer.getEmail().getAddress() +
                    " already requested deactivate");
        }
        else{
            accountTokenService.delete(accountToken);
        }

        String deactivateToken = accountTokenValueService.getTokenValue();
        accountTokenService.insert(developer.getName(), developer.getEmail(), AccountMode.DEACTIVATE, deactivateToken);
        emailService.sendAccountVerifyEmail(developer.getEmail(), deactivateToken, AccountMode.DEACTIVATE);

        return success(true);
    }
}
