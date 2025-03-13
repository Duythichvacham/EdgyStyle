<%-- 
    Document   : login.jsp
    Created on : Mar 8, 2025, 5:43:36 PM
    Author     : nttu2
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập - Edgy Style</title>
</head>
<body>
    <div class="login-container">
        <h2>Đăng nhập</h2>
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="Login">

            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Mật khẩu:</label>
            <input type="password" id="password" name="password" required>

            <button type="submit" class="btn-login">Đăng nhập</button>
        </form>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <p class="error-message"><%= error %></p>
        <%
            }
        %>

        <a class="register-link" href="register.jsp">Chưa có tài khoản? Đăng ký</a>
        <a class="register-link" href="forgotPassword.jsp">Forgot Password</a>
    </div>
</body>
</html>