/*
 * AsyncConfig.java
 * Author : 박찬형
 * Created Date : 2021-09-08
 */
package com.codrest.teriser.configures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfigure {
    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor(){
        return new ThreadPoolTaskExecutor();
    }
}
