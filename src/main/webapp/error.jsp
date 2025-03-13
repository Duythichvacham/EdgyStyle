<%-- 
    Document   : error
    Created on : Mar 1, 2022, 8:33:55 PM
    Author     : hd
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
    </head>
    <body>
        <%
            // Lấy thông báo lỗi từ request
            String errorMsg = (String) request.getAttribute("ERROR");
            if (errorMsg == null) {
                errorMsg = "Đã xảy ra lỗi không xác định.";
            }
        %>

        <div class="error-container">
            <h2>Oops! Có lỗi xảy ra</h2>
            <p class="error-message"><%= errorMsg%></p>
            <a class="back-link" href="dashboard.jsp">Quay về trang chính</a>
        </div>
    </body>
</html>
