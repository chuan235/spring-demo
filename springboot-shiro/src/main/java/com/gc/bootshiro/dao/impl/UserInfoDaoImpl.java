package com.gc.bootshiro.dao.impl;

import com.gc.bootshiro.dao.UserInfoDao;
import com.gc.bootshiro.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据操作实现类
 */
@Repository
public class UserInfoDaoImpl  implements UserInfoDao {

    @Autowired
    private JdbcTemplate template;

    @Override
    public UserInfo findByUserName(String username) {
        String sql = "select * from user_info where username=? and state=1";
        List<UserInfo> users = template.query(sql,new Object[]{username},new BeanPropertyRowMapper<>(UserInfo.class));
        UserInfo userInfo = null;
        if(users!=null && users.size()>0){
            userInfo = users.get(0);
        }
        return userInfo;
    }

    public JdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }
}
