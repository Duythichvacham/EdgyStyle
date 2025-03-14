/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass.OrderDetails;

/**
 *
 * @author nttu2
 */
import db.utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    
    public boolean addOrderDetail(OrderDetailDTO orderDetail) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO order_details (o_id, p_id, quantity, price_at, totalAmount) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderDetail.getO_id());
            ps.setInt(2, orderDetail.getP_id());
            ps.setInt(3, orderDetail.getQuantity());
            ps.setDouble(4, orderDetail.getPrice_At());
            ps.setDouble(5, orderDetail.getQuantity() * orderDetail.getPrice_At());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public List<OrderDetailDTO> getOrderDetailsByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        List<OrderDetailDTO> orderDetailList = new ArrayList<>();
        String sql = "SELECT o_id, p_id, quantity, price_at FROM order_details WHERE o_id = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetailDTO orderDetail = new OrderDetailDTO();
                    orderDetail.setO_id(rs.getInt("o_id"));
                    orderDetail.setP_id(rs.getInt("p_id"));
                    orderDetail.setQuantity(rs.getInt("quantity"));
                    orderDetail.setPrice_At(rs.getDouble("price_at"));
                    orderDetailList.add(orderDetail);
                }
            }
        }
        return orderDetailList;
    }
    
    public boolean updateOrderDetail(OrderDetailDTO orderDetail) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE order_details SET quantity = ?, price_at = ?, totalAmount = ? WHERE o_id = ? AND p_id = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderDetail.getQuantity());
            ps.setDouble(2, orderDetail.getPrice_At());
            ps.setDouble(3, orderDetail.getQuantity() * orderDetail.getPrice_At());
            ps.setInt(4, orderDetail.getO_id());
            ps.setInt(5, orderDetail.getP_id());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteOrderDetail(int orderId, int productId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM order_details WHERE o_id = ? AND p_id = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            
            return ps.executeUpdate() > 0;
        }
    }
}
