///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package ass.Controllers;
//
//import ass.Products.ProductDAO;
//import ass.Products.ProductDTO;
//import ass.Orders.OrderDAO;
//import ass.Orders.OrderDTO;
//import ass.OrderDetails.OrderDetailDAO;
//import ass.OrderDetails.OrderDetailDTO;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
///**
// *
// * @author nttu2
// */
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//public class CartController extends HttpServlet {
//
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//
//        String action = request.getParameter("action");
//        HttpSession session = request.getSession();
//
//        try {
//            ProductDAO productDAO = new ProductDAO();
//
//            // Initialize cart if it doesn't exist
//            if (session.getAttribute("cart") == null) {
//                session.setAttribute("cart", new HashMap<Integer, Integer>());
//            }
//
//            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
//
//            if ("AddToCart".equals(action)) {
//                int productId = Integer.parseInt(request.getParameter("productId"));
//                int quantity = Integer.parseInt(request.getParameter("quantity"));
//
//                // Add to cart
//                if (cart.containsKey(productId)) {
//                    cart.put(productId, cart.get(productId) + quantity);
//                } else {
//                    cart.put(productId, quantity);
//                }
//
//                session.setAttribute("cart", cart);
//                response.sendRedirect("ViewCart");
//            } else if ("ViewCart".equals(action)) {
//                List<ProductDTO> cartItems = new ArrayList<>();
//                double total = 0;
//
//                for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
//                    int productId = entry.getKey();
//                    int quantity = entry.getValue();
//
//                    ProductDTO product = productDAO.getProductById(productId);
//                    if (product != null) {
//                        product.setQuantity(quantity);
//                        cartItems.add(product);
//                        total += product.getPrice() * quantity;
//                    }
//                }
//
//                request.setAttribute("cartItems", cartItems);
//                request.setAttribute("total", total);
//                request.getRequestDispatcher("cart.jsp").forward(request, response);
//            } else if ("UpdateCart".equals(action)) {
//                String[] productIds = request.getParameterValues("productId");
//                String[] quantities = request.getParameterValues("quantity");
//
//                // Clear cart and add updated items
//                cart.clear();
//
//                if (productIds != null && quantities != null) {
//                    for (int i = 0; i < productIds.length; i++) {
//                        int productId = Integer.parseInt(productIds[i]);
//                        int quantity = Integer.parseInt(quantities[i]);
//
//                        if (quantity > 0) {
//                            cart.put(productId, quantity);
//                        }
//                    }
//                }
//
//                session.setAttribute("cart", cart);
//                response.sendRedirect("CartController?action=ViewCart");
//            } else if ("RemoveFromCart".equals(action)) {
//                int productId = Integer.parseInt(request.getParameter("productId"));
//                cart.remove(productId);
//
//                session.setAttribute("cart", cart);
//                response.sendRedirect("CartController?action=ViewCart");
//            } else if ("Checkout".equals(action)) {
//                // Check if user is logged in
//                if (session.getAttribute("u_id") == null) {
//                    request.setAttribute("message", "Please login to checkout");
//                    request.getRequestDispatcher("login.jsp").forward(request, response);
//                    return;
//                }
//
//                // Prepare checkout page
//                List<ProductDTO> cartItems = new ArrayList<>();
//                double total = 0;
//
//                for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
//                    int productId = entry.getKey();
//                    int quantity = entry.getValue();
//
//                    ProductDTO product = productDAO.getProductById(productId);
//                    if (product != null) {
//                        product.setQuantity(quantity);
//                        cartItems.add(product);
//                        total += product.getPrice() * quantity;
//                    }
//                }
//
//                request.setAttribute("cartItems", cartItems);
//                request.setAttribute("total", total);
//                request.getRequestDispatcher("checkout.jsp").forward(request, response);
//            } else if ("PlaceOrder".equals(action)) {
//                // Check if user is logged in
//                if (session.getAttribute("u_id") == null) {
//                    request.setAttribute("message", "Please login to place an order");
//                    request.getRequestDispatcher("login.jsp").forward(request, response);
//                    return;
//                }
//
//                int userId = (int) session.getAttribute("u_id");
//                double total = 0;
//
//                // Calculate total
//                for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
//                    int productId = entry.getKey();
//                    int quantity = entry.getValue();
//
//                    ProductDTO product = productDAO.getProductById(productId);
//                    if (product != null) {
//                        total += product.getPrice() * quantity;
//                    }
//                }
//
//                // Create order
//                OrderDAO orderDAO = new OrderDAO();
//                OrderDTO order = new OrderDTO();
//                order.setU_id(userId);
//                order.setTotatlPrice(total);
//                order.setStatus("pending");
//
//                int orderId = orderDAO.createOrder(order);
//
//                if (orderId > 0) {
//                    // Create order details
//                    OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
//
//                    for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
//                        int productId = entry.getKey();
//                        int quantity = entry.getValue();
//
//                        ProductDTO product = productDAO.getProductById(productId);
//                        if (product != null) {
//                            OrderDetailDTO orderDetail = new OrderDetailDTO();
//                            orderDetail.setO_id(orderId);
//                            orderDetail.setP_id(productId);
//                            orderDetail.setQuantity(quantity);
//                            orderDetail.setPrice_At(product.getPrice());
//
//                            orderDetailDAO.addOrderDetail(orderDetail);
//                        }
//                    }
//
//                    // Clear cart
//                    cart.clear();
//                    session.setAttribute("cart", cart);
//
//                    request.setAttribute("message", "Order placed successfully! Your order ID is: " + orderId);
//                    request.getRequestDispatcher("orderConfirmation.jsp").forward(request, response);
//                } else {
//                    request.setAttribute("error", "Failed to place order");
//                    request.getRequestDispatcher("checkout.jsp").forward(request, response);
//                }
//            } else {
//                // Default action: View cart
//                response.sendRedirect("CartController?action=ViewCart");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("ERROR", "Error processing cart: " + e.getMessage());
//            request.getRequestDispatcher("error.jsp").forward(request, response);
//        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
