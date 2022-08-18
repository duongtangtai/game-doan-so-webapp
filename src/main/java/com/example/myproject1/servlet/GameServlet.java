package com.example.myproject1.servlet;

import com.example.myproject1.model.GameSession;
import com.example.myproject1.model.Guess;
import com.example.myproject1.model.History;
import com.example.myproject1.model.Player;
import com.example.myproject1.service.GameService;
import com.example.myproject1.utils.JspUtils;
import com.example.myproject1.utils.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="GameServlet", urlPatterns = {
        UrlUtils.ROOT,
        UrlUtils.GAME,
        UrlUtils.XEP_HANG,
        UrlUtils.HISTORY,
        UrlUtils.HISTORY_ALL,
        UrlUtils.NEW_GAME
})
public class GameServlet extends HttpServlet {
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
                case UrlUtils.ROOT -> req.getRequestDispatcher(JspUtils.HOME).forward(req, resp);
                case UrlUtils.GAME -> {
                    req.setAttribute("stage","beforePlay");
                    req.getRequestDispatcher(JspUtils.GAME).forward(req,resp);
                }
                case UrlUtils.XEP_HANG -> processRank(req, resp);
                case UrlUtils.HISTORY -> processHistory(req, resp);
                case UrlUtils.NEW_GAME -> processNewGame(req, resp);
                default -> resp.sendRedirect(req.getContextPath() + UrlUtils.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String typeFunction = req.getParameter("typeFunction");
            switch (typeFunction) {
                case "start", "tryAgain" -> processPlayGame(req, resp);
                case "play" -> processGuess(req, resp);
                case "continue" -> processContinueGame(req, resp);
                default -> resp.sendRedirect(req.getContextPath() + UrlUtils.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void processPlayGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("stage","playing");
        returnGameSession(req, resp);
    }
    private void processGuess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Player player = (Player) req.getSession().getAttribute("currentUser");
        GameSession gameSession = service.getGameSession(player.getUsername());
        int guessNumber = Integer.parseInt(req.getParameter("guessNumber"));
        service.saveGuess(gameSession, guessNumber);
        if(guessNumber == gameSession.getTargetNumber()) { //if game is finished
            service.finishGame(gameSession); //finish game and set result to show
            req.setAttribute("stage","tryAgain");
            req.setAttribute("game", gameSession);
            req.setAttribute("guessList",service.getGuessList(gameSession));
            req.getRequestDispatcher(JspUtils.GAME).forward(req, resp);
        } else { // game is not finish, process play game
            service.updateGameSession(gameSession);
            processPlayGame(req, resp);
        }
    }
    private void processContinueGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Player player = (Player) req.getSession().getAttribute("currentUser");
        service.continueGame(player.getUsername(),req.getParameter("gameId"));
        processPlayGame(req, resp);
    }
    private void returnGameSession(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Player player = (Player) req.getSession().getAttribute("currentUser");
        GameSession gameSession = service.getGameSession(player.getUsername());
        List<Guess> guessList = service.getGuessList(gameSession);
        req.setAttribute("game",gameSession);
        req.setAttribute("guessList", guessList);
        req.getRequestDispatcher(JspUtils.GAME).forward(req, resp);
    }
    private void processRank(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("rankList", service.getRankList());
        req.getRequestDispatcher(JspUtils.XEP_HANG).forward(req, resp);
    }

    /**
     * The method processes pathInfo to see what user wants. If the path is valid, return history pages.
     * If not redirect to NOT_FOUND page
     */
    private void processHistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            getAHistoryPage(req, resp, currentPage);
        } else {
            String path = req.getPathInfo().substring(1);
            if (service.areAllNumbers(path)) {
                currentPage = Integer.parseInt(req.getPathInfo().substring(1));
                getAHistoryPage(req, resp, currentPage);
            } else {
                resp.sendRedirect(req.getContextPath() + UrlUtils.NOT_FOUND);
            }
        }
    }
    /**
     * The method will calculate the max number of history pages and the start number of a set of displayed oages.
     * The method returns player history based on the current page number.
     */
    private void getAHistoryPage(HttpServletRequest req, HttpServletResponse resp, int currentPage) throws ServletException, IOException {
        Player player = (Player) req.getSession().getAttribute("currentUser");
        int maxPages = service.getMaxHistoryPages(player.getUsername());
        if (currentPage > maxPages) { // if the requested page is higher than maxPages
            resp.sendRedirect(req.getContextPath() + UrlUtils.NOT_FOUND);
        } else {
            List<History> gameList = service.getPlayerHistory(player.getUsername(), currentPage);
            req.setAttribute("gameList", gameList);
            req.setAttribute("currentPage", currentPage);
            req.setAttribute("maxPages", maxPages);
            req.setAttribute("startPage", service.getHistoryStartPage(currentPage));
            req.getRequestDispatcher(JspUtils.HISTORY).forward(req, resp);
        }
    }
    private void processNewGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Player player = (Player) req.getSession().getAttribute("currentUser");
        service.createNewGame(player.getUsername());
        processPlayGame(req, resp);
    }
}
