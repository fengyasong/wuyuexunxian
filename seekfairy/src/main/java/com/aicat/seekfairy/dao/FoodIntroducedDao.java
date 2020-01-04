package com.aicat.seekfairy.dao;

import com.aicat.seekfairy.entity.FoodIntroduced;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FoodIntroducedDao {
    int deleteByPrimaryKey(Long id);

    int insert(FoodIntroduced record);

    FoodIntroduced selectByPrimaryKey(Long id);

    List<FoodIntroduced> selectAll();

    int updateByPrimaryKey(FoodIntroduced record);

    List<FoodIntroduced> list(Map<String, Object> map);
    int count(Map<String, Object> map);

    int batchRemove(Long[] ids);
}