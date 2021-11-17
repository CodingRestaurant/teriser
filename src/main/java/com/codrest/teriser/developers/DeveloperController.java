/*
 * Author : 나상혁 : Kasania, 박찬형
 * Filename : DeveloperController
 * Desc :
 */
package com.codrest.teriser.developers;

import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.security.JwtAuthentication;
import com.codrest.teriser.security.JwtAuthenticationToken;
import com.codrest.teriser.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static com.codrest.teriser.utils.ApiUtils.success;
import static com.codrest.teriser.security.AuthorityVerifier.verifyAuthority;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/developers")
public class DeveloperController {
    private final DeveloperService developerService;

    @GetMapping("")
    public ApiResult<DeveloperDto> getDeveloperInformation(JwtAuthenticationToken token) {
        verifyAuthority(token, new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN)));

        Developer developer = developerService.findById(((JwtAuthentication) token.getPrincipal()).id)
                .orElseThrow(() -> new NotFoundException("developer not found"));

        return success(Optional.of(developer).map(DeveloperDto::new).get());
    }
}
