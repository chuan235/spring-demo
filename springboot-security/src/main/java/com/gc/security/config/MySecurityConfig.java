package com.gc.security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 定制授权规则
        http.authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("VIP1")
                .antMatchers("/level2/**").hasRole("VIP2")
                .antMatchers("/level3/**").hasRole("VIP3");
        // 开启自动配置的登录功能 配置参数映射 配置登录的请求
        http.formLogin().usernameParameter("username").passwordParameter("pwd").loginPage("/userlogin");
        // 开启自动配置的注销功能
        // 访问 /logout 表示用户注销，清空session
        // 注销成功会返回 /login?logout 页面
        http.logout().invalidateHttpSession(false).logoutSuccessUrl("/").and().csrf().disable();
        // 开启记住我功能
        http.rememberMe().rememberMeParameter("remeber");
    }

    /**
     * 定义认证规则
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("zhangsan").password(new BCryptPasswordEncoder().encode("123")).roles("VIP1","VIP2")
                .and()
                .withUser("abc").password(new BCryptPasswordEncoder().encode("123")).roles("VIP2","VIP3")
                .and()
                .withUser("wangwu").password(new BCryptPasswordEncoder().encode("123")).roles("VIP3","VIP1");
    }
}
