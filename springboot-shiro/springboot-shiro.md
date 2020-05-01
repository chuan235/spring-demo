用户     ---> 角色 ----> 权限
userInfo     role      permission
    用户与角色之间：多对多关系
    角色与权限：多对多关系

shiro权限管理流程：[详见项目readme](https://github.com/Only-TEL/authority_management/blob/master/readMe.txt)
    1、用户认证
    2、用户授权


功能：
+ 用户的添加(1)
+ 用户的删除(2)
+ 用户的查询(3)

用户
+ 管理员(1,2,3)
+ 负责添加删除的人员(1,2,3)
+ 普通查询人员(3)

##SpringBoot集成Shiro
> SpringBoot集成与在Spring中使用shiro基本配置一样
> 只是将xml转变为java代码

+ 我们需要给shiro指定使用的加密方式（加密方式、盐、迭代次数）
  shiro内部提供的加密类型：
  + 对称加密：AES、BlowFish
  + 不可逆加密：SHA、MD5
  + 可逆加密：Hex、Base64
+ 加密方式
  + hex(salt)+hex(sha1(password,salt))配置迭代次数
  + md5(password,salt)配置迭代次数
  + salt多变


+ xml配置
```xml
<!-- 开启注解配置权限 -->
<!-- 开启AOP代理 -->
<aop:config proxy-target-class="true"></aop:config>
<!-- 开启shiro注解 -->
<bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">
    <property name="securityManager" ref="securityManager"></property>
</bean>
<!-- 配置shiro的过滤器 -->
<bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
    <property name="securityManager" ref="securityManager"></property>
    <!--如果没有指定要跳转的登陆地址，那就默认登陆地址为login.jsp -->
    <property name="loginUrl" value="/login.action"></property>
    <!--验证成功需要返回指定的地址  -->
    <property name="successUrl" value="/main.action"></property>
    <!-- 配置无权限访问时的页面 -->
    <property name="unauthorizedUrl" value="/refuse.jsp"></property>
    <!-- 配置处理结果的Filter -->
    <property name="filters">
        <map>
            <entry key="authc" value-ref="loginFormAuthFilter" />
        </map>
    </property>
    <!-- 定义访问资源的权限类型，对不同的资源设置不同的过滤器 -->
    <property name="filterChainDefinitions">
        <value>
            <!-- 授权 -->
            <!-- /sysmgr/queryDictList = perms[dict:query] -->
            <!--对静态资源进行匿名访问  -->
            /jsAndCss/** = anon
            /toLogin.action = anon
            /logout.action = logout
            <!--/** = authc 代表所有的url必须经过认证才可以访问  -->
            /** = authc
        </value>
    </property>
</bean>

<!-- 注册安全管理器 -->
<bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">
    <property name="realm" ref="userRealm"></property>
    <property name="cacheManager" ref="ehcacheManager"></property>
    <property name="sessionManager" ref="sessionManager"></property>
</bean>
<!-- 注册userRealm -->
<bean id="userRealm" class="com.zt.ssspm.security.UserRealm">
    <!-- 引入凭正匹配器 -->
    <property name="credentialsMatcher" ref="credentialsMatcher"></property>
    <property name="userService" ref="userService"></property>
    <property name="menuService" ref="menuService"></property>
    
</bean>
<!-- 配置凭正匹配器 -->
<bean id="credentialsMatcher" class="org.apache.shiro.authc.credential.HashedCredentialsMatcher">
    <property name="hashAlgorithmName" value="SHA-1" />
    <property name="hashIterations" value="1024" />
</bean>
<!-- 配置处理结果的过滤器 -->
<bean id="loginFormAuthFilter" class="com.zt.ssspm.security.LoginFormAuthticationFilter"></bean>
<!-- 配置ehcacheManager -->
<bean id="ehcacheManager" class="org.apache.shiro.cache.ehcache.EhCacheManager">
    <property name="cacheManagerConfigFile" value="classpath:ehcache.xml"></property>
</bean>
<!-- 定义sessionManager -->
<bean id="sessionManager" class="org.apache.shiro.web.session.mgt.DefaultWebSessionManager">
    <!-- 配置session的失效时间,单位是ms 1h -->
    <property name="globalSessionTimeout" value="36000000"></property>
    <!-- 定时清理失效的会话  30min -->
    <property name="sessionValidationInterval" value="1800000"></property>
</bean>
```

+ 使用java代码进行配置
```java
/**
 * Shiro配置类
 * @author HeadMaster
 */
@Configuration
public class ShiroConfig {

    /**
     * 开启shiro AOP注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 配置shiro的过滤器
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 配置登录页
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 配置首页
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 配置未授权跳转页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        // 定义访问资源的权限类型，对不同的资源设置不同的过滤器,从上往下进行筛选
        Map<String,String> filterChainMap = new LinkedHashMap<>();
        // 静态资源不进行认证  anno表示不需要认证
        filterChainMap.put("/static/**","anon");
        // 前往登录页面不拦截
        filterChainMap.put("/toLogin","anon");
        // 配置登出的认证过滤器
        filterChainMap.put("/logout","logout");
        // authc表示所有的请求都必须认证
        filterChainMap.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 配置安全管理器
     * @return
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    /**
     * 注册Shiro Realme
     * @return
     */
    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm realm = new MyShiroRealm();
        // 引入凭证匹配器
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

    /**
     * 配置凭正匹配器
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 使用散列算法 使用MD5加密
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 配置散列的次数md5(md5(md5(...)))
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }

    /**
     * 配置授权异常处理机制
     * @return
     */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
        SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        // 数据库异常处理
        mappings.setProperty("DatabaseException","databaseError");
        // 无权限处理
        mappings.setProperty("UnauthorizedException","403");
        // 将异常处理添加到异常映射器中
        simpleMappingExceptionResolver.setExceptionMappings(mappings);
        // 设置异常默认的视图
        simpleMappingExceptionResolver.setDefaultErrorView("error");
        // 设置异常默认的名称 默认为exception
        simpleMappingExceptionResolver.setExceptionAttribute("ex");
        // 配置错误异常格式
        //simpleMappingExceptionResolver.setWarnLogCategory("MvcLogger");
        return simpleMappingExceptionResolver;
    }
}

```






