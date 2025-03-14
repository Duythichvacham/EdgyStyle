/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass.Categories;

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

public class CategoryDAO {
    
    public List<CategoryDTO> getAllCategories() throws SQLException, ClassNotFoundException {
        List<CategoryDTO> categoryList = new ArrayList<>();
        String sql = "SELECT ct_id, ct_name FROM categories";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                CategoryDTO category = new CategoryDTO();
                category.setId(rs.getInt("ct_id"));
                category.setName(rs.getString("ct_name"));
                categoryList.add(category);
            }
        }
        return categoryList;
    }
    
    public CategoryDTO getCategoryById(int categoryId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT ct_id, ct_name FROM categories WHERE ct_id = ?";
        CategoryDTO category = null;
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    category = new CategoryDTO();
                    category.setId(rs.getInt("ct_id"));
                    category.setName(rs.getString("ct_name"));
                }
            }
        }
        return category;
    }
    
    public boolean addCategory(CategoryDTO category) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO categories (ct_name) VALUES (?)";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category.getName());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean updateCategory(CategoryDTO category) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE categories SET ct_name = ? WHERE ct_id = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category.getName());
            ps.setInt(2, category.getId());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteCategory(int categoryId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM categories WHERE ct_id = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;
        }
    }
}
