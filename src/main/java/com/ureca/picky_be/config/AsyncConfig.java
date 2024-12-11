package com.ureca.picky_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // thread-pool에 항상 살아있는 thread 최소 개수
        executor.setMaxPoolSize(5); // thread-pool에서 사용 가능한 최대 thread 개수
        executor.setQueueCapacity(500); // thread-pool에서 사용할 최대 queue 크기
        executor.setThreadNamePrefix("custom-task-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor customExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // thread-pool에 항상 살아있는 thread 최소 개수
        executor.setMaxPoolSize(5); // thread-pool에서 사용 가능한 최대 thread 개수
        executor.setQueueCapacity(500); // thread-pool에서 사용할 최대 queue 크기
        executor.setThreadNamePrefix("method-task");
        executor.initialize();
        return executor;
    }


}
