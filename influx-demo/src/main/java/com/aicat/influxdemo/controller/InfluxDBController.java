package com.aicat.influxdemo.controller;

import com.aicat.common.utils.R;
import com.aicat.influxdemo.config.InfluxDBConnect;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("influx")
@RestController
public class InfluxDBController {

    @Autowired
    InfluxDBConnect influxDBConnect;

    @GetMapping("query")
    R query(@RequestParam String sql) {
        QueryResult result = influxDBConnect.query(sql);
        //result.getResults()是同时查询多条sql的返回值
        List<QueryResult.Result> results = result.getResults();
        return R.ok().put("results", results);
    }
}
