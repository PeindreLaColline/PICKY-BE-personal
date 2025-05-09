package com.ureca.picky_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override()
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // thread-pool에 항상 살아있는 thread 최소 개수
        executor.setMaxPoolSize(5); // thread-pool에서 사용 가능한 최대 thread 개수
        executor.setQueueCapacity(500); // thread-pool에서 사용할 최대 queue 크기
        executor.setThreadNamePrefix("Default Async Executor");
        executor.initialize();
        return executor;
    }

    @Bean(name = "mailExecutor")
    public Executor mailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // thread-pool에 항상 살아있는 thread 최소 개수
        executor.setMaxPoolSize(5); // thread-pool에서 사용 가능한 최대 thread 개수
        executor.setQueueCapacity(500); // thread-pool에서 사용할 최대 queue 크기
        executor.setThreadNamePrefix("Email Executor");
        executor.initialize();
        return executor;
    }

    @Bean(name = "notificationExecutor")
    public Executor notificationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // thread-pool에 항상 살아있는 thread 최소 개수
        executor.setMaxPoolSize(5); // thread-pool에서 사용 가능한 최대 thread 개수
        executor.setQueueCapacity(500); // thread-pool에서 사용할 최대 queue 크기
        executor.setThreadNamePrefix("Notification Executor");
        executor.initialize();
//        return executor;
        return new DelegatingSecurityContextExecutor(executor);
    }


}
