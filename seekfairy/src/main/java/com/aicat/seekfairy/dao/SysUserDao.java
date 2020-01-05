package com.aicat.seekfairy.dao;

import com.aicat.seekfairy.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    List<SysUser> selectAll();

    int updateByPrimaryKey(SysUser record);

    //用户登录，根据用户名查找，user_name
    SysUser loginByUserName(String userName);
}