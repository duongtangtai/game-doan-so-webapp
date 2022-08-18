<%@ page import="com.example.myproject1.utils.UrlUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!doctype html>
<html lang="en">
<head>
    <title>Homepage</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
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
            <li class="nav-item active">
                <a class="nav-link font-weight-bold" href="<%=request.getContextPath() + UrlUtils.ROOT%>">Home<span
                        class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
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
        <ul class="dropdown-menu pr-5">
            <li><a class="dropdown-item" href="<%=request.getContextPath() + UrlUtils.DANG_XUAT%>">Đăng xuất</a></li>
        </ul>
    </div>
</nav>
<div class="container mt-5 border">
    <div class="row justify-content-left">
        <ul class="nav nav-tabs">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()+UrlUtils.ROOT%>">Thông tin cá nhân</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()+UrlUtils.CHANGE_PASSWORD%>">Đổi mật khẩu</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" aria-current="page">Lịch sử chơi game</a>
            </li>
        </ul>
    </div>
    <table class="table table-hover">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Màn Chơi</th>
            <th scope="col">Số lần đoán</th>
            <th scope="col">Thời gian bắt đầu</th>
            <th scope="col">Thời gian kết thúc</th>
            <th scope="col">Trạng thái</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="e" items="${gameList}" varStatus="num">
        <tr>
            <c:choose>
                <c:when test="${e.completed}"><c:set var="status" value="Hoàn thành"/></c:when>
                <c:otherwise><c:set var="status" value="Chưa hoàn thành"/></c:otherwise>
            </c:choose>
            <th scope="row">${num.index + 1 + (currentPage-1)*10}</th>
            <td>${e.gameSessionId}</td>
            <td>${e.tryTimes}</td>
            <td>${e.startTime.format(DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/yy"))}</td>
            <td>${e.endTime.format(DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/yy"))}</td>
            <td>${status}</td>
            <td>
                <c:if test="${status.equals('Chưa hoàn thành')}">
                    <form action="<%=request.getContextPath() + UrlUtils.GAME%>" method="post">
                        <input type="hidden" name="typeFunction" value="continue">
                        <input type="hidden" name="gameId" value="${e.gameSessionId}">
                        <button type="submit" class="btn btn-outline-primary">Tiếp tục</button>
                    </form>
                </c:if>
            </td>
        </tr>
        </c:forEach>
        </tbody>
    </table>
    <div>
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <li class="page-item ${currentPage==1?'disabled':''}">
                    <a class="page-link"
                       href="<%=request.getContextPath() + UrlUtils.HISTORY +"/"%>${currentPage-1}">Trang Trước</a>
                </li>
                <c:choose>
                    <c:when test="${maxPages == startPage}">
                        <li class="page-item active"><a class="page-link">${startPage}</a></li>
                    </c:when>
                    <c:when test ="${maxPages <= startPage + 4}">
                        <c:forEach begin="${startPage}" end="${maxPages}" var="i">
                            <c:if test="${currentPage == i}">
                                <li class="page-item active"><a class="page-link">${i}</a></li>
                            </c:if>
                            <c:if test="${currentPage != i}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="<%=request.getContextPath() + UrlUtils.HISTORY +"/"%>${i}">${i}</a>
                                </li>
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach begin="${startPage}" end="${startPage + 4}" var="i">
                            <c:choose>
                                <c:when test="${currentPage==i}">
                                    <li class="page-item active"><a class="page-link">${i}</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item">
                                        <a class="page-link"
                                           href="<%=request.getContextPath() + UrlUtils.HISTORY +"/"%>${i}">${i}</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                <li class="page-item ${currentPage==maxPages?'disabled':''}">
                    <a class="page-link"
                       href="<%=request.getContextPath() + UrlUtils.HISTORY +"/"%>${currentPage+1}">Trang Sau</a>
                </li>
            </ul>
        </nav>
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
