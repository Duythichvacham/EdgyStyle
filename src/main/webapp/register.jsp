<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Register</title>
        <link rel="stylesheet" type="text/css" href="CSS/register.css"/>
    </head>

    <body>
        <h2>Register New Account</h2>

        <%-- Hiển thị thông báo lỗi nếu có --%>
        <% if (request.getAttribute("ERROR") != null) {%>
        <div class="error">
            <%= request.getAttribute("ERROR")%>
        </div>
        <% } %>

        <%-- Hiển thị thông báo thành công nếu có --%>
        <% if (request.getAttribute("SUCCESS") != null) {%>
        <div class="success">
            <%= request.getAttribute("SUCCESS")%>
        </div>
        <% }%>

        <form action="./MainController" method="POST">
            <input type="hidden" name="action" value="register" >

            <div class="register-form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>
            </div>

            <div class="register-form-group">
                <label for="fullName">Full Name:</label>
                <input type="text" id="fullName" name="fullName" required>
            </div>

            <div class="register-form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>

            <div class="register-form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>

            <div class="register-form-group">
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>

            <div class="register-form-group">
                <label for="phone">Phone:</label>
                <input type="text" id="phone" name="phone">
            </div>

            <div class="register-form-group">
                <label for="address">Address:</label>
                <input type="text" id="address" name="address">
            </div>

            <div class="register-form-group">
                <input type="submit" value="Register" class="submit-btn"/>
            </div>
        </form>

        <p>Already have an account? <a href="login.jsp">Login here</a></p>
    </body>
</html>