package top.gmfcj.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

@Component
public class LogFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 这里就表明这是一个什么类型的过滤器
        // pre  route   post    error
        // return "pre";
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        // 配置filter的执行顺序,数值越小越先执行
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        // 是否应用这个filer
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 主要处理逻辑
        RequestContext context = RequestContext.getCurrentContext();
        String routeUri = context.get(FilterConstants.REQUEST_URI_KEY).toString();
        HttpServletRequest request = context.getRequest();
        // 访问者ip=0:0:0:0:0:0:0:1,请求URI=/api/user/api/power/feign,路由后的地址:/api/power/feign
        // 访问者ip=0:0:0:0:0:0:0:1,请求URI=/api/user/power/feign,路由后的地址:/power/feign
        System.out.println("访问者ip="+request.getRemoteAddr()+",请求URI="+request.getRequestURI()+",路由后的地址:"+routeUri);
        return null;
    }
}
