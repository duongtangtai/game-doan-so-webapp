<%@ page import="com.example.myproject1.utils.UrlUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
        <ul class="dropdown-menu">
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
            <li class="nav-item" aria-current="page">
                <a class="nav-link active">Đổi mật khẩu</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()+UrlUtils.HISTORY%>">Lịch sử chơi game</a>
            </li>
        </ul>
    </div>
    <form action="<%=request.getContextPath() + UrlUtils.CHANGE_PASSWORD%>" method="post">
    <div class="col justify-content-left m-3">
        <div class="form-group">
            <label for="oldPw">Mật khẩu cũ:</label>
            <input type="text" class="form-control" name="oldPw" id="oldPw" required autofocus autocomplete="off">
        </div>
        <div class="form-group">
            <label for="newPw">Mật khẩu mới:</label>
            <input type="text" class="form-control" name="newPw" id="newPw" required autofocus autocomplete="off">
            <small class="form-text text-muted">Mật khẩu không có dấu và không chứa ký tự đặc biệt</small>
        </div>
        <div class="form-group">
            <label for="repeatNewPw">Xác nhận lại mật khẩu mới:</label>
            <input type="text" class="form-control" name="repeatNewPw" id="repeatNewPw" required autofocus autocomplete="off">
        </div>
        <button type="submit" class="btn btn-primary">Đổi mật khẩu</button>
        <c:if test="${errors!=null}">
            <div class="alert alert-danger mt-3">
                ${errors}
            </div>
        </c:if>
        <c:if test="${info!=null}">
            <div class="alert alert-success mt-3">
                    ${info}
            </div>
        </c:if>
    </div>
    </form>
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
