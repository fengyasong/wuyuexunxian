package com.aicat.seekfairy.service;

import com.aicat.seekfairy.entity.SysUser;

public interface UserService extends BaseService<SysUser> {

    SysUser loginByUserName(String userName);
}
