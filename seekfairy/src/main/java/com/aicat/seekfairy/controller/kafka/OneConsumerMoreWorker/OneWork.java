/*
package com.aicat.seekfairy.controller.kafka.OneConsumerMoreWorker;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

*/
/**
 * 多线程kafka消费类
 *//*

public class OneWork implements Runnable {
    //日志类
    private transient final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ConsumerRecord<String, String> consumerRecord;

    public OneWork(ConsumerRecord record) {
        this.consumerRecord = record;
    }

    @Override
    public void run() {
        try{
            //执行消费数据处理方法consumerRecord.value()--消费数据
            //SendVehicleInfo.jsonToWebService(consumerRecord.value());
            LOG.info("value= "+consumerRecord.value());
        }catch (Exception e){
            LOG.info("异常错误信息："+e.getMessage());
        }
    }

}
*/
