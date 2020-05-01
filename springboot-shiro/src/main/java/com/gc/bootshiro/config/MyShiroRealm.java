package com.gc.bootshiro.config;

import com.gc.bootshiro.dao.impl.SysPermissionDao;
import com.gc.bootshiro.dao.impl.SysRoleDao;
import com.gc.bootshiro.model.SysPermission;
import com.gc.bootshiro.model.SysRole;
import com.gc.bootshiro.model.UserInfo;
import com.gc.bootshiro.service.UserInfoService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author HeadMaster
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysPermissionDao sysPermissionDao;

    /**
     * 身份认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("开始身份认证 ---> doGetAuthenticationInfo");
        // 获取用户名
        String loginName = (String)authenticationToken.getPrincipal();
        // 查询数据库
        UserInfo userInfo = userInfoServiceImpl.findByUserName(loginName);
        System.out.println("用户名："+userInfo);
        System.out.println("password:"+authenticationToken.getCredentials());
        System.out.println("----->userInfo="+userInfo);
        if(userInfo != null){
            // 参数：主体对象(数据库对象)，密码，加密的salt，realm Name
            // 加密算法new SimpleHash("md5/sha1",password明文,salt盐,hashInter迭代次数)
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                    userInfo,
                    userInfo.getPassword(),
                    ByteSource.Util.bytes(userInfo.getCredentialsSalt()),
                    this.getName()
            );
            return info;
        }
        return null;
    }

    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("开始授权-->MyShiroRealm.doGetAuthorizationInfo()");
        // 获取主体对象，是认证方法传递过来的对象
        UserInfo userInfo = (UserInfo)principals.getPrimaryPrincipal();
        // 存放授权信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        userInfo.setRoleList(sysRoleDao.findRoles(userInfo.getUid()));
        // 获取角色信息
        for(SysRole role:userInfo.getRoleList()){
            // 添加角色信息：admin vip deptManager ...
            info.addRole(role.getRole());
            // 添加权限信息：userInfo:view,userInfo:add ...
            role.setPermissions(sysPermissionDao.findPermissions(role.getId()));
            for(SysPermission p:role.getPermissions()){
                System.out.println("权限："+p.getPermission());
                info.addStringPermission(p.getPermission());
            }
        }
        return info;
    }

    public UserInfoService getUserInfoServiceImpl() {
        return userInfoServiceImpl;
    }

    public void setUserInfoServiceImpl(UserInfoService userInfoServiceImpl) {
        this.userInfoServiceImpl = userInfoServiceImpl;
    }

    public SysRoleDao getSysRoleDao() {
        return sysRoleDao;
    }

    public void setSysRoleDao(SysRoleDao sysRoleDao) {
        this.sysRoleDao = sysRoleDao;
    }

    public SysPermissionDao getSysPermissionDao() {
        return sysPermissionDao;
    }

    public void setSysPermissionDao(SysPermissionDao sysPermissionDao) {
        this.sysPermissionDao = sysPermissionDao;
    }
}
