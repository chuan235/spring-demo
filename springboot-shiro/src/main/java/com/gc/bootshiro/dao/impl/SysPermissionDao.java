package com.gc.bootshiro.dao.impl;

import com.gc.bootshiro.model.SysPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 查询权限
 */
@Repository
public class SysPermissionDao {

    @Autowired
    private JdbcTemplate template;

    public JdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    /**
     * 根据角色id查询权限列表
     * @param roleId
     * @return
     */
    public List<SysPermission> findPermissions(Integer roleId){
        String sql = "select * from sys_permission where id in (select permission_id from sys_role_permission where role_id=?)";
        return template.query(sql, new Object[]{roleId}, new BeanPropertyRowMapper<>(SysPermission.class));
    }


}
