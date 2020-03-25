/*
package com.aicat.seekfairy.controller.kafka.Factory;

import com.aicat.seekfairy.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController("kafka")
@Slf4j
public class TopicController {

    @Autowired
    private AdminClient adminClient;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @GetMapping("findAllTopic")
    R findAllTopic(@RequestParam Map<String, Object> params){
        ListTopicsResult result = adminClient.listTopics();
        Collection<TopicListing> list = null;
        try {
            list = result.listings().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<String> resultList = new ArrayList<>();
        for(TopicListing topicListing : list){
            resultList.add(topicListing.name());
        }
        return R.ok().put("resultList",resultList);
    }
    @GetMapping("info")
    R info(@RequestParam String topicName) throws ExecutionException, InterruptedException {
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList(topicName));
        Map<String,String> resultMap = new HashMap<>();
        result.all().get().forEach((k,v)->{
            log.info("k: "+k+" ,v: "+v.toString());
            resultMap.put(k,v.toString());
        });
        return R.ok().put("resultMap",resultMap);
    }
    @GetMapping("send")
    R sendMessage(@RequestParam String topic,@RequestParam String data){
        kafkaTemplate.send(topic,data);
        return R.ok();
    }
}
*/
