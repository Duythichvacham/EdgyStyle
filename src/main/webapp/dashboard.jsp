<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Dashboard - Edgy Style</title>
        <link rel="stylesheet" type="text/css" href="css/edgyStyle.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <%
            // Kiểm tra session và role
            HttpSession mySession = request.getSession(false);
            if (mySession == null || mySession.getAttribute("u_id") == null) {
                // Nếu chưa đăng nhập, chuyển về login
                response.sendRedirect("login.jsp");
                return;
            }
            String full_name = (String) mySession.getAttribute("full_name");
            String role = (String) mySession.getAttribute("role");
        %>
        
        <div class="dashboard-container">
            <header class="dashboard-header">
                <div class="logo">
                    <h1>Edgy Style</h1>
                </div>
                <div class="user-info">
                    <span class="user-greeting">Xin chào, <%= full_name%>!</span>
                    <span class="user-role">(<%= role%>)</span>
                    <a href="MainController?action=Logout" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a>
                </div>
            </header>
            
            <div class="dashboard-content">
                <nav class="dashboard-sidebar">
                    <%
                        // Nếu là Admin (ADM)
                        if ("Admin".equals(role)) {
                    %>
                    <div class="sidebar-category">
                        <h3>Quản lý người dùng</h3>
                        <ul>
                            <li><a href="MainController?action=ViewAllUsers"><i class="fas fa-users"></i> Danh sách người dùng</a></li>
                            <li><a href="MainController?action=AddStaff"><i class="fas fa-user-plus"></i> Thêm nhân viên</a></li>
                        </ul>
                    </div>
                    
                    <div class="sidebar-category">
                        <h3>Quản lý sản phẩm</h3>
                        <ul>
                            <li><a href="MainController?action=ViewProducts"><i class="fas fa-tshirt"></i> Danh sách sản phẩm</a></li>
                            <li><a href="MainController?action=AddProductForm"><i class="fas fa-plus-circle"></i> Thêm sản phẩm mới</a></li>
                            <li><a href="MainController?action=ManageCategories"><i class="fas fa-tags"></i> Quản lý danh mục</a></li>
                        </ul>
                    </div>
                    
                    <div class="sidebar-category">
                        <h3>Quản lý đơn hàng</h3>
                        <ul>
                            <li><a href="MainController?action=ViewAllOrders"><i class="fas fa-shopping-cart"></i> Tất cả đơn hàng</a></li>
                            <li><a href="MainController?action=ViewPendingOrders"><i class="fas fa-clock"></i> Đơn hàng chờ xử lý</a></li>
                            <li><a href="MainController?action=ViewShippedOrders"><i class="fas fa-shipping-fast"></i> Đơn hàng đang giao</a></li>
                            <li><a href="MainController?action=ViewCompletedOrders"><i class="fas fa-check-circle"></i> Đơn hàng hoàn thành</a></li>
                            <li><a href="MainController?action=ViewCancelledOrders"><i class="fas fa-times-circle"></i> Đơn hàng đã hủy</a></li>
                        </ul>
                    </div>
                                       
                    <%
                    } else if ("User".equals(role)) {
                    %>
                    <div class="sidebar-category">
                        <h3>Tài khoản của tôi</h3>
                        <ul>
                            <li><a href="MainController?action=ViewProfile"><i class="fas fa-user"></i> Thông tin cá nhân</a></li>
                            <li><a href="MainController?action=UpdateProfile"><i class="fas fa-user-edit"></i> Cập nhật thông tin</a></li>
                            <li><a href="MainController?action=ChangePassword"><i class="fas fa-key"></i> Đổi mật khẩu</a></li>
                        </ul>
                    </div>
                    
                    <div class="sidebar-category">
                        <h3>Mua sắm</h3>
                        <ul>
                            <li><a href="MainController?action=ViewProducts"><i class="fas fa-shopping-bag"></i> Tất cả sản phẩm</a></li>
                            <li><a href="MainController?action=ViewCategories"><i class="fas fa-list"></i> Danh mục sản phẩm</a></li>
                            <li><a href="MainController?action=ViewCart"><i class="fas fa-shopping-cart"></i> Giỏ hàng</a></li>
                            <li><a href="MainController?action=Wishlist"><i class="fas fa-heart"></i> Sản phẩm yêu thích</a></li>
                        </ul>
                    </div>
                    
                    <div class="sidebar-category">
                        <h3>Đơn hàng</h3>
                        <ul>
                            <li><a href="MainController?action=ViewMyOrders"><i class="fas fa-clipboard-list"></i> Đơn hàng của tôi</a></li>
                            <li><a href="MainController?action=TrackOrder"><i class="fas fa-truck"></i> Theo dõi đơn hàng</a></li>
                            <li><a href="MainController?action=OrderHistory"><i class="fas fa-history"></i> Lịch sử đơn hàng</a></li>
                        </ul>
                    </div>
                    <%
                    }
                    %>
                </nav>
                
                <main class="dashboard-main">
                    <div class="welcome-section">
                        <h2>Chào mừng đến với Edgy Style!</h2>
                        <%
                            if ("Admin".equals(role)) {
                        %>
                        <div class="dashboard-stats">
                            <div class="stat-card">
                                <i class="fas fa-users"></i>
                                <h3>Người dùng</h3>
                                <p class="stat-number"><!-- Số liệu thống kê --></p>
                            </div>
                            <div class="stat-card">
                                <i class="fas fa-shopping-cart"></i>
                                <h3>Đơn hàng</h3>
                                <p class="stat-number"><!-- Số liệu thống kê --></p>
                            </div>
                            <div class="stat-card">
                                <i class="fas fa-tshirt"></i>
                                <h3>Sản phẩm</h3>
                                <p class="stat-number"><!-- Số liệu thống kê --></p>
                            </div>
                            <div class="stat-card">
                                <i class="fas fa-dollar-sign"></i>
                                <h3>Doanh thu</h3>
                                <p class="stat-number"><!-- Số liệu thống kê --></p>
                            </div>
                        </div>
                        
                        <div class="recent-activity">
                            <h3>Hoạt động gần đây</h3>
                            <div class="activity-list">
                                <!-- Danh sách hoạt động gần đây sẽ được hiển thị ở đây -->
                            </div>
                        </div>  
                        <%
                            } else if ("User".equals(role)) {
                        %>
                        <div class="user-welcome">
                            <p>Cảm ơn bạn đã mua sắm cùng Edgy Style. Khám phá các bộ sưu tập mới nhất của chúng tôi!</p>
                            <div class="action-buttons">
                                <a href="MainController?action=ViewProducts" class="action-btn shop-now">Mua sắm ngay</a>
                                <a href="MainController?action=ViewCart" class="action-btn view-cart">Xem giỏ hàng</a>
                            </div>
                        </div>
                        
                        <div class="featured-products">
                            <h3>Sản phẩm nổi bật</h3>
                            <div class="product-carousel">
                                <!-- Sản phẩm nổi bật sẽ được hiển thị ở đây -->
                            </div>
                        </div>
                        <%
                            }
                        %>
                    </div>
                </main>
            </div>
            
            <footer class="dashboard-footer">
                <p>&copy; 2025 Edgy Style. Tất cả các quyền được bảo lưu.</p>
            </footer>
        </div>

    </body>
</html>