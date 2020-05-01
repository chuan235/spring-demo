package com.gc.bootshiro.dao.impl;

import com.gc.bootshiro.model.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 查询角色
 */
@Repository
public class SysRoleDao {

    @Autowired
    private JdbcTemplate template;

    public JdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    /**
     * 根据用户id查询角色列表
     * @param userId
     * @return
     */
    public List<SysRole> findRoles(Integer userId){
        String sql = "select * from sys_role where id in (select role_id from sys_user_role where uid=?)";
        return template.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(SysRole.class));
    }


}
