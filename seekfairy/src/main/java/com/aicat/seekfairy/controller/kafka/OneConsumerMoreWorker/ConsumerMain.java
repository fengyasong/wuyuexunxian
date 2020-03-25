/*
package com.aicat.seekfairy.controller.kafka.OneConsumerMoreWorker;

public class ConsumerMain {


    public static void main(String[] args) {
        String servers = "localhost:9092,localhost:9093,localhost:9094";
        String commit = "true";
        String intervalms = "1000";
        String groupId = "group2";
        String topic = "test-topic";
        int num = 5;


        //开启消费的方法
        ConsumerHandler consumers = new ConsumerHandler(servers, commit, intervalms, "1000", groupId, topic);
        //反射多线程消费任务类传入执行方法
        try {
            Class<?> cl = Class.forName("com.demo.kafka.impl.OneWork");
            //开始消费 num线程数量 topic 消费主题
            consumers.execute(num, cl, topic);
            Thread.sleep(1000000);
        } catch (InterruptedException ignored) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        consumers.shutdown();

    }
    */
/*public void del(int consumerNum){
        for (int i = 0; i < consumerNum; i++) {

            //根据属性创建Consumer
            final Consumer<String, byte[]> consumer = consumerFactory.getConsumer(getServers(), groupId);
            //consumerList.add(consumer);

            //订阅主题Arrays.asList(topic)
            consumer.subscribe(Arrays.asList(topic));

            //consumer.poll()拉取数据
            BufferedConsumerRecords bufferedConsumerRecords = new BufferedConsumerRecords(consumer);

            getExecutor().scheduleWithFixedDelay(() -> {
                long startTime = System.currentTimeMillis();

                //进行消息处理
                consumeEvents(bufferedConsumerRecords);

                long sleepTime = intervalMillis - (System.currentTimeMillis() - startTime);
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);
        }
    }*//*


}
*/
