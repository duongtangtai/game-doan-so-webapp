package com.example.myproject1.servlet;

import com.example.myproject1.utils.JspUtils;
import com.example.myproject1.utils.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="ErrorHandleServlet", urlPatterns = {
        UrlUtils.NOT_FOUND,
        UrlUtils.INTERNAL_ERROR
})
public class ErrorHandleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getServletPath()) {
            case UrlUtils.NOT_FOUND -> req.getRequestDispatcher(JspUtils.NOT_FOUND).forward(req, resp);
            case UrlUtils.INTERNAL_ERROR -> req.getRequestDispatcher(JspUtils.INTERNAL_ERROR).forward(req, resp);
        }
    }
}
