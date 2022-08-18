package com.example.myproject1.filter;

import com.example.myproject1.utils.UrlUtils;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "InternalErrorFilter", urlPatterns = UrlUtils.ALL)
public class InternalErrorFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if(resp.getStatus() == 500){
            resp.sendRedirect(req.getContextPath() + UrlUtils.INTERNAL_ERROR);
        } else {
            chain.doFilter(req, resp);
        }
    }
}
