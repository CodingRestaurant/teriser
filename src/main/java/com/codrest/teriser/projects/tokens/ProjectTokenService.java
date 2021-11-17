/*
 * Author : Hyeokwoo Kwon
 * Filename : ProjectTokenService.java
 * Desc :
 */

package com.codrest.teriser.projects.tokens;

import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.errors.UnauthorizedException;
import com.codrest.teriser.utils.SecureTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProjectTokenService {

    private static final int POOL_SIZE = 100;
    private static final int REFILL_PERCENTAGE = 50;

    private final ProjectTokenRepository tokenPool;
    private final AtomicLong numAvailable;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public ProjectTokenService(ProjectTokenRepository projectTokenRepository) {
        this.tokenPool = projectTokenRepository;
        numAvailable = new AtomicLong();

        fillTokenPool();
    }

    @Async
    public void fillTokenPool() {
        long numToFill = POOL_SIZE - numAvailable.get();
        try {
            List<CompletableFuture<String>> tasks = new ArrayList<>();
            for (long i = 0; i < numToFill; ++i) {
                tasks.add(generateToken());
            }

            CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
                    .thenAccept(consumer -> {
                        final List<String> tokens = new ArrayList<>();
                        for (CompletableFuture<String> task : tasks) {
                            try {
                                tokens.add(task.get());
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                        saveBulkToken(tokens);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * /projects/{projectID}/token 엔트포인트에서 호출되는 함수
     * 프로젝트에 지정되어 있던 토큰을 폐기함
     *
     * @param projectToken project에 지정되어 있던 토큰
     */
    @Transactional
    public void expire(ProjectToken projectToken) {
        projectToken.setExpiredDate(LocalDateTime.now());
        tokenPool.save(projectToken);
    }

    /**
     * /projects/{projectID}/token 엔드포인트에서 호출되는 함수
     * 토큰을 사용 처리한 뒤 발급함
     *
     * @return project에 지정될 토큰
     */
    @Transactional
    public String issue() {
        long seq = tokenPool.findMinIdUnissuedToken();
        ProjectToken projectToken = tokenPool.findById(seq).orElseThrow(
                () -> new NotFoundException("Could not issue new token.")
        );
        projectToken.setIssuedDate(LocalDateTime.now());
        tokenPool.save(projectToken);

        // 가용한 토큰의 개수가 토큰풀의 10% 밑으로 내려가면 토큰풀을 채움
        if (numAvailable.decrementAndGet() < POOL_SIZE * REFILL_PERCENTAGE / 100) {
            fillTokenPool();
        }

        return projectToken.getToken();
    }

    public void expireBy(String token) {
        ProjectToken projectToken = tokenPool.findByToken(token).orElseThrow(
                () -> new UnauthorizedException("Invalid token.")
        );
        expire(projectToken);
    }

    @Async
    public CompletableFuture<String> generateToken() {
        String token = SecureTokenGenerator.generateToken();
        while (tokenPool.existsByToken(token)) {
            token = SecureTokenGenerator.generateToken();
        }
        return new AsyncResult<>(token).completable();
    }

    @Transactional
    public void saveBulkToken(List<String> tokens) {
        List<ProjectToken> projectTokens = new ArrayList<>();
        for (String token : tokens) {
            projectTokens.add(ProjectToken.builder()
                    .token(token)
                    .createdDate(LocalDateTime.now())
                    .build());
        }
        tokenPool.saveAll(projectTokens);

        numAvailable.addAndGet(tokens.size());
    }
}
