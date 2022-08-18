package com.example.myproject1.filter;

import com.example.myproject1.utils.UrlUtils;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = UrlUtils.ALL)
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        //check two conditions, if user already logged in, the if statement will return TRUE immediately
        //they can go whatever page it is. If the user didn't log in, the if statement will check the
        //2nd condition, if the user want to access insecure pages, then it's fine to let them in. The
        //if statement, then, will return TRUE, or return ELSE if they want to access secure pages
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (isLoginUser(req) || isAuthUrl(req)) { //check two conditions, if user already logged in
            chain.doFilter(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + UrlUtils.DANG_NHAP);
        }
    }
    private boolean isLoginUser(HttpServletRequest req) {
        var currentUser = req.getSession().getAttribute("currentUser");
        return currentUser != null;
    }
    private boolean isAuthUrl(HttpServletRequest req) {
        var path = req.getServletPath();
        return path.equals(UrlUtils.DANG_KY) || path.equals(UrlUtils.DANG_NHAP);
    }
}
