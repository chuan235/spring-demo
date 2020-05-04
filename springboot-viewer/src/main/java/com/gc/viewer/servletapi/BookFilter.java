package com.gc.viewer.servletapi;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/book/filter")
public class BookFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("=====BookFilter.doFilter============");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
