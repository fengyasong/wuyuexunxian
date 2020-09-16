package com.aicat.seekfairy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorConfig {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors()*2;

    //创建线程池
    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("test-pool");
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize*2);
        executor.setQueueCapacity(16);//缓存队列
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//拒绝策略，由调用者线程执行
        executor.setWaitForTasksToCompleteOnShutdown(true);//所有任务结束后再关闭线程池
        executor.setKeepAliveSeconds(60);
        executor.initialize();
        return executor;
    }
}
