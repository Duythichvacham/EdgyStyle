/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass.Orders;

/**
 *
 * @author nttu2
 */
import ass.OrderDetails.OrderDetailDAO;
import ass.OrderDetails.OrderDetailDTO;
import db.utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int createOrder(OrderDTO order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO orders (total_price, status, u_id) VALUES (?, ?, ?)";
        int orderId = -1;

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, order.getTotatlPrice());
            ps.setString(2, order.getStatus());
            ps.setInt(3, order.getU_id());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                }
            }
        }
        return orderId;
    }

    public OrderDTO getOrderById(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT o_id, ordered_at, total_price, status, u_id FROM orders WHERE o_id = ?";
        OrderDTO order = null;

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = new OrderDTO();
                    order.setId(rs.getInt("o_id"));
                    order.setOrdered_At(rs.getDate("ordered_at"));
                    order.setTotatlPrice(rs.getDouble("total_price"));
                    order.setStatus(rs.getString("status"));
                    order.setU_id(rs.getInt("u_id"));
                }
            }
        }
        return order;
    }

    public List<OrderDTO> getOrdersByUserId(int userId) throws SQLException, ClassNotFoundException {
        List<OrderDTO> orderList = new ArrayList<>();
        String sql = "SELECT o_id, ordered_at, total_price, status, u_id FROM orders WHERE u_id = ? ORDER BY ordered_at DESC";

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDTO order = new OrderDTO();
                    order.setId(rs.getInt("o_id"));
                    order.setOrdered_At(rs.getDate("ordered_at"));
                    order.setTotatlPrice(rs.getDouble("total_price"));
                    order.setStatus(rs.getString("status"));
                    order.setU_id(rs.getInt("u_id"));
                    orderList.add(order);
                }
            }
        }
        return orderList;
    }

    public List<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        List<OrderDTO> orderList = new ArrayList<>();
        String sql = "SELECT o_id, ordered_at, total_price, status, u_id FROM orders ORDER BY ordered_at DESC";

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderDTO order = new OrderDTO();
                order.setId(rs.getInt("o_id"));
                order.setOrdered_At(rs.getDate("ordered_at"));
                order.setTotatlPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setU_id(rs.getInt("u_id"));
                orderList.add(order);
            }
        }
        return orderList;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE orders SET status = ? WHERE o_id = ?";

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean cancelOrder(int orderId) throws SQLException, ClassNotFoundException {
        return updateOrderStatus(orderId, "cancelled");
    }
}
