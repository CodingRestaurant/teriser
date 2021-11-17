/*
 * Author : 나상혁 : Kasania, 박찬형
 * Filename : DeveloperService
 * Desc :
 */
package com.codrest.teriser.developers;

import com.codrest.teriser.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
public class DeveloperService {
    private final DeveloperRepository developerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<Developer> findById(Long developerId) {
        checkNotNull(developerId, "developerId must be provided.");

        return developerRepository.findById(developerId);
    }

    @Transactional(readOnly = true)
    public boolean isActivatedDeveloper(Email email){
        return developerRepository.existsByEmailAndActivateIsTrue(email);
    }

    @Transactional
    public Developer insert(Developer developer){
        if(developer.getLoginToken() != null){
            developer.setLoginToken(passwordEncoder.encode(developer.getLoginToken()));
        }
        return developerRepository.save(developer);
    }

    @Transactional(readOnly = true)
    public Optional<Developer> findByEmail(Email email){
        return developerRepository.findByEmail(email);
    }

    @Transactional
    public void updateLoginToken(Developer developer, String loginToken){
        developer.setLoginToken(passwordEncoder.encode(loginToken));
        update(developer);
    }

    @Transactional
    public void update(Developer developer){
        developerRepository.save(developer);
    }

    @Transactional(readOnly = true)
    public Developer login(Email email, String loginToken) {
        checkNotNull(loginToken, "password must be provided");
        Developer developer = findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Could not found user for " + email.getAddress()));
        developer.login(passwordEncoder, loginToken);

        return developer;
    }
}
