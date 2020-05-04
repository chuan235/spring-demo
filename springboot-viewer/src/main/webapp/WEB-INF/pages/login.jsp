<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
        <title>登陆页面</title>
    </head>
    <body>
        <h3>Spring Boot 登陆测试</h3>
        <form method="post" action="/user/login">
            用户名：<input name="username" type="text" /><br><br>
            密 码：<input name="password" type="password" /><br><br>
            <input type="submit" value="提交">
        </form>
    </body>
</html>