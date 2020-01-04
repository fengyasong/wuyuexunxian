package com.aicat.seekfairy.dao;

import com.aicat.seekfairy.entity.Scenery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SceneryDao {
    int deleteByPrimaryKey(Long id);

    int insert(Scenery record);

    Scenery selectByPrimaryKey(Long id);

    List<Scenery> selectAll();

    int updateByPrimaryKey(Scenery record);
}