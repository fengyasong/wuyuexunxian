/*
package com.aicat.seekfairy.controller.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaConsumerPool {

    */
/**
     * 日志处理
     *//*

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerPool.class);

    */
/**
     *  线程池
     *//*

    private ExecutorService executor;

    */
/**
     * 初始化10个线程
     *//*

    @PostConstruct
    void init(){
        //executor = Executors.newFixedThreadPool(10);
        executor = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    */
/**
     * 提交新的消费者
     *
     * @param shutdownableThread
     *//*

    public void SubmitConsumerPool(ShutdownableThread shutdownableThread) {
        executor.execute(shutdownableThread);
    }

    */
/**
     * 程序关闭,关闭线程池
     *
     *//*

    @PreDestroy
    void fin(){
        shutdown();
    }

    public void shutdown() {
        if (executor != null) executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                log.info("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            log.info("Interrupted during shutdown, exiting uncleanly");
        }
    }
}
*/
