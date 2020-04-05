package com.aicat.mybatisplus.dao;

import com.aicat.mybatisplus.entity.Food;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodDao extends BaseMapper<Food> {

}