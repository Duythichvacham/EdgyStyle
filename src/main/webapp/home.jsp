<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ</title>
    <link rel="stylesheet" rel="text/css" href="CSS/home.css"/>
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
            <!-- Thanh tìm kiếm -->
            <form action="ProductController" method="get" style="display: inline-flex; align-items: center;">
                <input type="hidden" name="action" value="SearchProducts">
                <input type="text" class="search-bar" name="keyword" placeholder="Tìm kiếm..." value="${param.keyword}">
                <button type="submit" style="background: none; border: none; padding: 5px; cursor: pointer;">🔍</button>
            </form>
            <span class="cart-icon">🛒</span>
        </div>
    </nav>
    <div class="video-section">
        <div class="banner-ad-left">
            <p>Banner Quảng Cáo 160x600px (Trái)</p>
        </div>
        <div class="video-background">
            <video autoplay loop muted playsinline>
                <source src="./videos/home.mp4" type="video/mp4">
            </video>
        </div>
        <div class="banner-ad-right">
            <p>Banner Quảng Cáo 160x600px (Phải)</p>
        </div>
    </div>
    <div class="content">
        <h2>Tìm kiếm sản phẩm</h2>
        <p>Vui lòng nhập tiêu chí tìm kiếm để xem sản phẩm.</p>

        <!-- Form tìm kiếm -->
        <form class="search-form" action="ProductController" method="get">
            <input type="hidden" name="action" value="SearchProducts">
            <input type="text" name="keyword" placeholder="Từ khóa (tên/mô tả)" value="${param.keyword}">
            <select name="category">
                <option value="">Tất cả danh mục</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.ct_id}" ${param.category == category.ct_id ? 'selected' : ''}>${category.ct_name}</option>
                </c:forEach>
            </select>
            <input type="number" name="minPrice" placeholder="Giá tối thiểu" value="${param.minPrice}" step="0.01">
            <input type="number" name="maxPrice" placeholder="Giá tối đa" value="${param.maxPrice}" step="0.01">
            <input type="number" name="minQuantity" placeholder="Số lượng tối thiểu" value="${param.minQuantity}">
            <input type="text" name="color" placeholder="Màu sắc" value="${param.color}">
            <button type="submit">Tìm kiếm</button>
        </form>

        <!-- Hiển thị thông báo lỗi nếu có -->
        <c:if test="${not empty ERROR}">
            <p class="error">${ERROR}</p>
        </c:if>

        <!-- Hiển thị kết quả tìm kiếm nếu có -->
        <c:if test="${not empty searchResults}">
            <h3>Kết quả tìm kiếm</h3>
            <c:if test="${empty searchResults}">
                <p>Không tìm thấy sản phẩm nào phù hợp với tiêu chí.</p>
            </c:if>
            <c:if test="${not empty searchResults}">
                <div class="product-list">
                    <c:forEach var="product" items="${searchResults}">
                        <div class="product">
                            <h3>${product.name}</h3>
                            <p>${product.description}</p>
                            <p>Giá: <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/></p>
                            <p>Kích thước: ${product.size}</p>
                            <p>Màu sắc: ${product.color}</p>
                            <p>Số lượng: ${product.quantity}</p>
                            <c:set var="images" value="${requestScope['images_' + product.id]}" />
                            <c:if test="${not empty images}">
                                <c:forEach var="img" items="${images}">
                                    <img src="${img.imgUrl}" alt="Ảnh sản phẩm">
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty images}">
                                <p>Chưa có ảnh.</p>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </c:if>
    </div>
</body>
</html>