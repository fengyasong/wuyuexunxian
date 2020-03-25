package com.aicat.seekfairy.service;

import com.aicat.seekfairy.entity.FoodIntroduced;

import java.util.Map;
import java.util.Set;

public interface FoodIntroService extends BaseService<FoodIntroduced> {

    int count(Map<String, Object> map);

    Set<Integer> getIds();
}
