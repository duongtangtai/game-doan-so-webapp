<%@ page import="com.example.myproject1.service.Result" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.example.myproject1.utils.UrlUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!doctype html>
<html lang="en">
<head>
    <title>Đoán Số</title>
    <!-- Required meta tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #e3f2fd;">
    <a class="navbar-brand font-weight-bold" href="#">Trò Chơi Đoán Số</a>
    <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link font-weight-bold" href="<%=request.getContextPath() + UrlUtils.ROOT%>">Home<span
                        class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item active">
                <a class="nav-link font-weight-bold" href="<%=request.getContextPath() + UrlUtils.GAME%>">Current Game</a>
            </li>
            <li class="nav-item">
                <a class="nav-link font-weight-bold"
                   href="<%=request.getContextPath() + UrlUtils.XEP_HANG%>">Ranking</a>
            </li>
            <li class="nav-item">
                <a class="nav-link font-weight-bold"
                   href="<%=request.getContextPath() + UrlUtils.NEW_GAME%>">New Game</a> <%--call doGet method--%>
            </li>
        </ul>
    </div>
    <div class="nav-item dropdown">
        <a class="nav-link dropdown-toggle font-weight-bold" href="#" role="button" data-toggle="dropdown"
           aria-expanded="false">
            ${sessionScope.currentUser.name}
        </a>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="<%=request.getContextPath() + UrlUtils.DANG_XUAT%>">Đăng xuất</a></li>
        </ul>
    </div>
</nav>
<div class="container">
    <div class="row justify-content-center mt-5">
        <div class="col-md-8">
            <c:if test="${fn:contains('playing',stage)}">
            <h2 class="text text-primary text-center">MỜI BẠN ĐOÁN SỐ</h2>
            </c:if>
        </div>
    </div>
    <div class="row justify-content-center">
        <div class="col-md-8">
            <form action="<%=request.getContextPath() + UrlUtils.GAME%>" method="post">
                <c:if test="${fn:contains('beforePlay', stage)}"> <%--BEFORE PLAY STAGE--%>
                    <div class="form-row align-items-center">
                        <input type="hidden" name="typeFunction" value="start">
                        <button type="submit" class="btn btn-outline-primary btn-lg col-4 offset-4">Bắt đầu</button>
                    </div>
                </c:if>
                <c:if test="${fn:contains('playing',stage)}"> <%--PLAYING STAGE--%>
                <div class="form-group form-row">
                    <label for="number"></label>
                    <input type="number" class="form-control form-control-lg text-center col-4 offset-4" id="number"
                        name="guessNumber" required min="1" max="1000" autofocus>
                </div>
                <div class="form-row align-items-center">
                    <input type="hidden" name="typeFunction" value="play">
                    <button type="submit" class="btn btn-outline-primary btn-lg col-4 offset-4">Đoán</button>
                </div>
                </c:if>
                <c:if test="${fn:contains('tryAgain', stage)}"> <%--TRY AGAIN STAGE--%>
                    <h2 class="text text-success text-center">BINGO!!! BINGO!!! BINGO!!!</h2>
                <div class="form-row align-items-center">
                    <input type="hidden" name="typeFunction" value="tryAgain">
                    <button type="submit" class="btn btn-outline-primary btn-lg col-4 offset-4">Chơi lại</button>
                </div>
                </c:if>
            </form>
        </div>
    </div>
    <div class="row justify-content-center mt-5">
        <div class="col-md-8"> <%--Show GAMEID in playing & tryAgain stage --%>
            <c:if test="${fn:contains('playing', stage) || fn:contains('tryAgain', stage)}">
                ${game.id}
            </c:if>
            <c:if test="${guessList.size() > 0}">
                <table class="table table-borderless">
                    <thead>
                    <tr>
                        <th scope="col">Lần đoán</th>
                        <th scope="col">Số đoán</th>
                        <th scope="col">Kết quả</th>
                        <th scope="col">Thời gian</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="num" value="${guessList.size()}"/>
                    <c:forEach var="e" items="${guessList}">
                        <c:choose>
                            <c:when test="${fn:contains(Result.GREATER_THAN,e.result)}">
                                <tr class="table-danger">
                                    <th scope="row">${num}</th>
                                    <td>${e.guessNumber}</td>
                                    <td>${e.result}</td>
                                    <td>${e.time.format(DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/yyyy"))}</td>
                                </tr>
                            </c:when>
                            <c:when test="${fn:contains(Result.LESS_THAN,e.result)}">
                                <tr class="table-warning">
                                    <th scope="row">${num}</th>
                                    <td>${e.guessNumber}</td>
                                    <td>${e.result}</td>
                                    <td>${e.time.format(DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/yyyy"))}</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr class="table-success">
                                    <th scope="row">${num}</th>
                                    <td>${e.guessNumber}</td>
                                    <td>${e.result}</td>
                                    <td>${e.time.format(DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/yyyy"))}</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        <c:set var="num" value="${num - 1}"/>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
</body>
</html>
