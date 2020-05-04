package com.gc.viewer.container;

import org.apache.catalina.Context;
import org.apache.catalina.core.StandardContext;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.stereotype.Component;

/**
 * 编码配置内嵌容器
 * 日志
 * 线程数
 * 端口
 * 地址
 * ...
 */
@Component
public class MyContainer implements TomcatContextCustomizer {

    @Override
    public void customize(Context context) {

        StandardContext standardContext = (StandardContext) context;
    }
}
