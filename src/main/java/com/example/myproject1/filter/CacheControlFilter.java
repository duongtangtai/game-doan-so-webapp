package com.example.myproject1.filter;

import com.example.myproject1.utils.UrlUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CacheControlFilter", urlPatterns = {
        UrlUtils.ROOT,
        UrlUtils.GAME,
        UrlUtils.XEP_HANG,
        UrlUtils.HISTORY,
        UrlUtils.CHANGE_PASSWORD,
        UrlUtils.NEW_GAME
})
public class CacheControlFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
        chain.doFilter(request, response);
    }
}
