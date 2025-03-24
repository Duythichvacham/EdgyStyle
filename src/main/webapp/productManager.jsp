<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Sản Phẩm</title>
    <style>
       
    </style>
</head>
<body>
    <h1>QUẢN LÝ SẢN PHẨM</h1>
    <!-- Định nghĩa productList từ requestScope - chưa có-->
    <c:set var="productList" value="${requestScope.productList}" />
    <!-- Bảng hiển thị danh sách sản phẩm -->
    <table>
        <thead>
            <tr>
                <th>Tên Sản Phẩm</th>
                <th>Mô Tả</th>
                <th>Giá</th>
                <th>Ảnh</th>
                <th>Màu Sắc</th>
                <th>Thương Hiệu</th>
                <th>Kích Thước</th>
                <th>Số Lượng</th>
                <th>Hành Động</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty productList}">
                    <tr class="empty-row">
                        <td colspan="9">Không có sản phẩm nào để hiển thị.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="product" items="${productList}">
                        <tr>
                            <td>${product.name}</td>
                            <td>${product.description}</td>
                            <td><fmt:formatNumber value="${product.price}" type="number" groupingUsed="true"/>đ</td>
                            <td class="images">
                                <c:forEach var="img" items="${product.imageUrl.split(',')}">
                                    <img src="${img.trim()}" alt="Ảnh sản phẩm">
                                </c:forEach>
                            </td>
                            <td>${product.color}</td>
                            <td>${product.brand}</td>
                            <td>${product.size}</td>
                            <td>${product.quantity}</td>
                            <td class="action-buttons">
                                <a href="ProductServlet?action=edit&id=${product.id}">Sửa</a> |
                                <a href="ProductServlet?action=delete&id=${product.id}" 
                                   onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <!-- Form thêm sản phẩm mới -->
    <div class="add-product-form">
        <h3>THÊM SẢN PHẨM MỚI</h3>
        <form action="ProductServlet" method="post">
            <input type="hidden" name="action" value="add">
            
            <label>Tên sản phẩm:</label><br>
            <input type="text" name="name" required><br>

            <label>Mô tả:</label><br>
            <textarea name="description" required></textarea><br>

            <label>Giá:</label><br>
            <input type="number" name="price" step="0.01" required><br>

            <label>URL Ảnh (phân tách bằng dấu phẩy):</label><br>
            <input type="file" name="images" multiple accept="image/*" required><br>

            <label>Màu sắc:</label><br>
            <input type="text" name="color" required><br>

            <label>Thương hiệu:</label><br>
            <input type="text" name="brand" required><br>
            
            <label>ID Danh mục (Category ID):</label><br>
            <input type="number" name="ct_id" required><br>
            
            <label>Kích thước:</label><br>
            <input type="text" name="size" required><br>

            <label>Số lượng:</label><br>
            <input type="number" name="quantity" required><br>

            <button type="submit">Thêm Sản Phẩm</button>
        </form>
    </div>
</body>
</html>