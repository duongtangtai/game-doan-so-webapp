package com.example.myproject1.servlet;

import com.example.myproject1.model.Player;
import com.example.myproject1.service.GameService;
import com.example.myproject1.utils.JspUtils;
import com.example.myproject1.utils.UrlUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebServlet(name="AuthServlet", urlPatterns = {
        UrlUtils.DANG_KY,
        UrlUtils.DANG_NHAP,
        UrlUtils.DANG_XUAT,
        UrlUtils.CHANGE_PASSWORD
})
public class AuthServlet extends HttpServlet {
    private GameService service;
    @Override
    public void init() throws ServletException {
        super.init();
        service = GameService.getService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            switch (req.getServletPath()) {
                case UrlUtils.DANG_KY -> req.getRequestDispatcher(JspUtils.DANG_KY).forward(req, resp);
                case UrlUtils.DANG_NHAP -> req.getRequestDispatcher(JspUtils.DANG_NHAP).forward(req, resp);
                case UrlUtils.DANG_XUAT -> processLogout(req, resp);
                case UrlUtils.CHANGE_PASSWORD -> req.getRequestDispatcher(JspUtils.CHANGE_PASSWORD).forward(req, resp);
                default -> resp.sendRedirect(req.getContextPath() + UrlUtils.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            switch (req.getServletPath()) {
                case UrlUtils.DANG_KY -> processRegister(req, resp);
                case UrlUtils.DANG_NHAP -> processLogin(req, resp);
                case UrlUtils.CHANGE_PASSWORD -> processChangePW(req, resp);
                default -> resp.sendRedirect(req.getContextPath() + UrlUtils.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Player oldPlayer = service.checkLogin(username, password);
        if (oldPlayer!= null) { //if oldPlayer exists -> set player on session and get a gameSession
            req.getSession().setAttribute("currentUser", oldPlayer); //set player on session
            req.setAttribute("game",service.getGameSession(username)); //get game Session
            resp.sendRedirect(req.getContextPath() + UrlUtils.GAME);
        } else { // if oldPlayer doesn't exists -> send error to show
            req.setAttribute("errors","Username or password is incorrect");
            req.getRequestDispatcher(JspUtils.DANG_NHAP).forward(req, resp);
        }
    }

    private void processRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        Player player = service.registerPlayer(username, password, name); //nullable
        if (player==null) { //send error to show
            req.setAttribute("errors", "Username already used or containing special characters.");
            req.getRequestDispatcher(JspUtils.DANG_KY).forward(req, resp);
        } else { // if register successfully
            req.getSession().setAttribute("currentUser", player);
            resp.sendRedirect(req.getContextPath() + UrlUtils.GAME);
        }
    }

    private void processLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("currentUser");
        session.removeAttribute("stage");
        session.invalidate();
        resp.sendRedirect(req.getContextPath() + UrlUtils.DANG_NHAP);
    }
    private void processChangePW(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oldPw = req.getParameter("oldPw");
        String newPw = req.getParameter("newPw");
        String repeatNewPw = req.getParameter("repeatNewPw");
        Player player = (Player) req.getSession().getAttribute("currentUser");
        if (!oldPw.equals(player.getPassword())) {
            req.setAttribute("errors","Mật khẩu cũ không đúng!");
        } else if (!service.isValidCharacter(newPw)) {
            req.setAttribute("errors","Mật khẩu mới chứa ký tự đặc biệt!");
        } else if (!newPw.equals(repeatNewPw)) {
            req.setAttribute("errors","Nhập lại mật khẩu mới không chính xác");
        } else {
            player.setPassword(newPw);
            service.updatePlayer(player);
            req.setAttribute("info","Thay đổi mật khẩu thành công");
        }
        req.getRequestDispatcher(JspUtils.CHANGE_PASSWORD).forward(req, resp);
    }
}
