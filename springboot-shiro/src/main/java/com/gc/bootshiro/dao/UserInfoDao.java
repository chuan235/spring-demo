package com.gc.bootshiro.dao;

import com.gc.bootshiro.model.UserInfo;

/**
 * 用户信息操作DAO
 */
public interface UserInfoDao{

    /**
     * 根据用户名查询用户对象 -> 用户名/账号/电话号码
     * @param username
     * @return
     */
    UserInfo findByUserName(String username);

}
