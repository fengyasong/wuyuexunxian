/*
package com.aicat.seekfairy.controller.kafka.Factory;

import cn.hutool.json.JSONUtil;
import com.aicat.seekfairy.entity.Food;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class KafkaListeners {

    @KafkaListener(containerFactory = "kafkaBatchListener6",topics = {"#{'${spring.kafka.listener.topics}'.split(',')[0]}"})
    public void batchListener(List<ConsumerRecord<?,?>> records, Acknowledgment ack){

        List<Food> userList = new ArrayList<>();
        try {
            records.forEach(record -> {
                Food user = JSONUtil.toBean(record.value().toString(),Food.class);
                userList.add(user);
                log.info("record.value()={}",record.value());
            });
        } catch (Exception e) {
            log.error("Kafka监听异常"+e.getMessage(),e);
        } finally {
            ack.acknowledge();//手动提交偏移量
        }

    }

}

*/
