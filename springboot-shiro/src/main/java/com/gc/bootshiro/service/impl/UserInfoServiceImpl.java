package com.gc.bootshiro.service.impl;

import com.gc.bootshiro.dao.UserInfoDao;
import com.gc.bootshiro.model.UserInfo;
import com.gc.bootshiro.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户信息服务实现类
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDaoImpl;

    @Override
    public UserInfo findByUserName(String userName) {
        System.out.println("UserInfoServiceImpl.findByUsername()");
        UserInfo userInfo = userInfoDaoImpl.findByUserName(userName);
        return userInfo;
    }

    public UserInfoDao getUserInfoDaoImpl() {
        return userInfoDaoImpl;
    }

    public void setUserInfoDaoImpl(UserInfoDao userInfoDaoImpl) {
        this.userInfoDaoImpl = userInfoDaoImpl;
    }

}
