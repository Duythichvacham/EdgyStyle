<%-- 
    Document   : home
    Created on : Mar 13, 2025, 9:43:39 PM
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ</title>
    <link rel="stylesheet" type="text/css" href="./CSS/home.css">

</head>
<body>
    <nav class="navbar">
        <div class="nav-left">
            <h2>LOGO</h2>
            <a href="#">Bán chạy</a>
            <a href="#">Nam</a>
            <a href="#">Nữ</a>
        </div>
        <div class="nav-right">
            <input type="text" class="search-bar" placeholder="Tìm kiếm...">
            <span class="cart-icon">🛒</span>
        </div>
    </nav>
    <div class="hero-section">
        <video autoplay loop muted playsinline>
            <source src="./videos/home.mp4" type="video/mp4">
        </video>
    </div>
    <div class="content">
        <h2>Sản phẩm nổi bật</h2>
        <p>Danh sách các sản phẩm sẽ được hiển thị tại đây.</p>
    </div>
</body>
</html>

