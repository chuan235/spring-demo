### 生成SSL证书

```text
http传输协议是裸漏的，明文传输的，极易被黑客拦截。因此，想到了加密，也就是对称加密，不过这个由于因为对称加密需要每个客户端和服务器有独立一套，当客户端多的时候维护困难，因此有了非对称加密。非对称加密有一个缺点就是：当服务器发送给客户端的时候，被黑客拦截了，用公开的公钥解密，是可以看到里面的内容的。 因此后续有了SSL涉及SSL证书。由于SSL技术已建立到所有主要的浏览器和WEB服务器程序中，因此，仅需安装服务器证书就可以激活该功能了），即通过它可以激活SSL协议，实现数据信息在客户端和服务器之间的加密传输，可以防止数据信息的泄露，保证了双方传递信息的安全性，而且用户可以通过服务器证书验证他所访问的网站是否是真实可靠。数字签名又名数字标识、签章 (即 Digital Certificate，Digital ID )，提供了一种在网上进行身份验证的方法，是用来标志和证明网络通信双方身份的数字信息文件，概念类似日常生活中的司机驾照或身份证。 数字签名主要用于发送安全电子邮件、访问安全站点、网上招标与投标、网上签约、网上订购、网上公文安全传送、网上办公、网上缴费、网上缴税以及网上购物等安全的网上电子交易活动。
```

1、生成SSL证书

```shell
keytool -genkeypair -alias tomcat -keyalg RSA -keystore E:\tomcat.key
```

2、建立普通的springboot项目

![](images/structural.png)

3、配置springboot配置文件

```yml
server:
  port: 8081
  tomcat:
    accept-count: 3000
    max-threads: 800
    min-spare-threads: 20
    max-connections: 30000
  servlet:
    context-path: /ssl
  ssl:
    # key的路径
    key-alias: tomcat
    key-store: classpath:tomcat.key
    key-store-type: JKS
    # 配置密码 生成证书的时候输入的密码
    key-store-password: password
```

4、启动项目

输入https://localhost:8081/ssl进入首页。

