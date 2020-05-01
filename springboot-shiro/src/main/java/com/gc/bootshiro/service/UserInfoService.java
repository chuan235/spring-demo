package com.gc.bootshiro.service;

import com.gc.bootshiro.model.UserInfo;

/**
 * 用户操作接口
 */
public interface UserInfoService {

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    UserInfo findByUserName(String userName);


}
