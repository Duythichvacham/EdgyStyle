<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Sản Phẩm</title>
        <link rel="stylesheet" type="text/css" href="CSS/productManager.css"/>
    </head>
    <body>
        <h1>QUẢN LÝ SẢN PHẨM</h1>

        <!-- Thông báo thành công hoặc lỗi -->
        <c:if test="${not empty SUCCESS}">
            <p style="color: green;">${SUCCESS}</p>
        </c:if>
        <c:if test="${not empty ERROR}">
            <p style="color: red;">${ERROR}</p>
        </c:if>

        <!-- Bảng hiển thị danh sách sản phẩm -->
        <table>
            <thead>
                <tr>
                    <th>Tên Sản Phẩm</th>
                    <th>Mô Tả</th>
                    <th>Giá</th>
                    <th>Ảnh</th>
                    <th>Màu Sắc</th>
                    <th>Kích Thước</th>
                    <th>Số Lượng</th>
                    <th>Danh Mục ID</th>
                    <th>Tool</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty products}">
                        <tr class="empty-row">
                            <td colspan="9">Không có sản phẩm nào để hiển thị.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <td>${product.name}</td>
                                <td>${product.description}</td>
                                <td><fmt:formatNumber value="${product.price}" type="number" groupingUsed="true"/>đ</td>
                                <td class="images">
                                    <c:if test="${not empty product.img}">
                                        <img src="${pageContext.request.contextPath}/${product.img}" alt="Ảnh sản phẩm">
                                    </c:if>
                                </td>
                                <td>${product.color}</td>
                                <td>${product.size}</td>
                                <td>${product.stock_quantity}</td>
                                <td>${product.ct_id}</td>
                                <td class="tool-buttons">
                                    <a href="ProductController?action=ViewProductDetails&productId=${product.p_id}">Edit</a>
                                    <form action="ProductController" method="post" onsubmit="return confirm('Bạn có chắc muốn xóa sản phẩm này?');">
                                        <input type="hidden" name="action" value="DeleteProduct">
                                        <input type="hidden" name="productId" value="${product.p_id}">
                                        <button type="submit">Delete</button>
                                    </form>
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
            <form action="ProductController" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="AddProduct">

                <label>Tên sản phẩm:</label>
                <input type="text" name="name" required>

                <label>Mô tả:</label>
                <textarea name="description" required></textarea>

                <label>Giá:</label>
                <input type="number" name="price" step="0.01" required>

                <label>Ảnh:</label>
                <input type="file" name="images" accept="image/*" required>

                <label>Màu sắc:</label>
                <input type="text" name="color" required>

                <label>Kích thước:</label>
                <input type="text" name="size" required>

                <label>Số lượng:</label>
                <input type="number" name="stock_quantity" required>

                <label>Danh mục ID:</label>
                <input type="number" name="ct_id" required>

                <button type="submit">Thêm Sản Phẩm</button>
            </form>
        </div>
    </body>
</html>