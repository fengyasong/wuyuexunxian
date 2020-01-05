package com.aicat.seekfairy.service.impl;

import com.aicat.seekfairy.dao.SysUserDao;
import com.aicat.seekfairy.entity.SysUser;
import com.aicat.seekfairy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    SysUserDao userDao;

    @Override
    public List<SysUser> findAll() {
        return userDao.selectAll();
    }

    @Override
    public SysUser findById(Long id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public int create(SysUser user) {
        user.setGmt_create(new Date());
        user.setStatus((byte)1);
        user.setName(user.getUser_name());
        user.setGmt_modified(new Date());
        return userDao.insert(user);
    }

    @Override
    public int delete(Long id) {
        return userDao.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long... ids) {
        return 0;
    }

    @Override
    public int update(SysUser user) {
        user.setGmt_modified(new Date());
        return userDao.updateByPrimaryKey(user);
    }

    @Override
    public SysUser loginByUserName(String userName) {
        return userDao.loginByUserName(userName);
    }
}
