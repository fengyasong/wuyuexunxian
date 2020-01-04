package com.aicat.seekfairy.dao;

import com.aicat.seekfairy.entity.SceneryIntroduced;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SceneryIntroducedDao {
    int deleteByPrimaryKey(Long id);

    int insert(SceneryIntroduced record);

    SceneryIntroduced selectByPrimaryKey(Long id);

    List<SceneryIntroduced> selectAll();

    int updateByPrimaryKey(SceneryIntroduced record);
}