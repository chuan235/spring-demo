package com.gc.bootshiro.model;

import javax.persistence.*;
import java.util.List;

/**
 * 角色类
 */
@Entity
public class SysRole {

    /**
     * 编号
     */
    @Id
    @GeneratedValue
    private Integer id;
    /**
     * 角色标识程序中判断使用,如"admin",这个是唯一的:
     */
    private String role;
    /**
     * 角色描述,UI界面显示使用
     */
    private String description;
    /**
     * 是否可用,如果不可用将不会添加给用户
     */
    private Boolean available = Boolean.FALSE;

    /**
     * 一个角色对应多个权限
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="sysRolePermission",joinColumns = {@JoinColumn(name="roleId")},inverseJoinColumns = {@JoinColumn(name="permissionId")})
    private List<SysPermission> permissions;

    /**
     * 一个角色对应多个用户
     */
    @ManyToMany
    @JoinTable(name="sysUserRole",joinColumns = {@JoinColumn(name="roleId")},inverseJoinColumns = {@JoinColumn(name="uid")})
    private List<UserInfo> userInfoList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
}
