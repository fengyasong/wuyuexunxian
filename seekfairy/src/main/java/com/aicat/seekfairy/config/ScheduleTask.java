//package com.aicat.seekfairy.config;
//
//import com.aicat.seekfairy.service.FoodIntroService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.*;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ThreadPoolExecutor;
//
//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
//@EnableAsync
//public class ScheduleTask implements SchedulingConfigurer {
//
//    @Autowired
//    FoodIntroService foodIntroService;
//    /**
//     * 创建线程池
//     */
//    @Bean
//    public Executor executor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setThreadNamePrefix("test-schedule");
//        executor.setMaxPoolSize(20);
//        executor.setCorePoolSize(10);
//        executor.setQueueCapacity(5);// 缓存队列
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        return executor;
//    }
//
//    /**
//     *  Cron表达式参数分别表示：
//     * 秒（0~59） 例如0/5表示每5秒
//     * 分（0~59）
//     * 时（0~23）
//     * 月的某天（0~31） 需计算
//     * 月（0~11）
//     * 周几（ 可填1-7 或 SUN/MON/TUE/WED/THU/FRI/SAT）
//     *
//     * 0 0 1 * * ?	每天1点0分0秒执行一次
//     */
//    @Async
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void configureTasks() {
//        System.err.println("执行定时任务1: " + LocalDateTime.now());
//    }
//    @Async
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void configureTasks2() {
//        System.err.println("执行定时任务2: " + LocalDateTime.now());
//    }
//        /**
//         * 执行定时任务.
//         */
//    @Async
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.addTriggerTask(
//                //1.添加任务内容(Runnable)
//                () -> {
//                    Set<Integer> ids = foodIntroService.getIds();
//                    ids.forEach(id -> System.out.println("id = " + id));
//                    System.out.println("执行动态定时任务: " + foodIntroService.count(null));
//                },
//                //2.设置执行周期(Trigger)
//                triggerContext -> {
//                    //执行周期
//                    String cron = "*/6 * * * * ?";
//                    //返回执行周期(Date)
//                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
//                }
//        );
//    }
//}
