package com.aicat.seekfairy.dao;

import com.aicat.seekfairy.entity.Food;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FoodDao {
    int deleteByPrimaryKey(Long id);

    int insert(Food record);

    Food selectByPrimaryKey(Long id);

    List<Food> selectAll();

    int updateByPrimaryKey(Food record);

    List<Food> list(Map<String, Object> map);
    int count(Map<String, Object> map);
}