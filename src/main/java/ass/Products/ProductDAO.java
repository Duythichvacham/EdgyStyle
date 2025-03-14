/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass.Products;

/**
 *
 * @author nttu2
 */
import ass.Categories.CategoryDAO;
import ass.Categories.CategoryDTO;
import db.utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<ProductDTO> getAllProducts() throws SQLException, ClassNotFoundException {
        List<ProductDTO> productList = new ArrayList<>();
        String sql = "SELECT p_id, name, description, price, image_url, size, color, brand, ct_id, quantity FROM products";

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setId(rs.getInt("p_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setImageUrl(rs.getString("image_url"));
                product.setSize(rs.getString("size"));
                product.setColor(rs.getString("color"));
                product.setBrand(rs.getString("brand"));
                product.setCt_id(rs.getInt("ct_id"));
                product.setCt_id(rs.getInt("quantity"));
                productList.add(product);
            }
        }
        return productList;
    }

    public ProductDTO getProductById(int productId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT p_id, name, description, price, image_url, size, color, brand, ct_id, quantity FROM products WHERE p_id = ?";
        ProductDTO product = null;

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = new ProductDTO();
                    product.setId(rs.getInt("p_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setImageUrl(rs.getString("image_url"));
                    product.setSize(rs.getString("size"));
                    product.setColor(rs.getString("color"));
                    product.setBrand(rs.getString("brand"));
                    product.setCt_id(rs.getInt("ct_id"));
                    product.setCt_id(rs.getInt("quantity"));

                }
            }
        }
        return product;
    }

    public List<ProductDTO> searchProducts(String keyword, String category, Double minPrice, Double maxPrice, int minQuantity,
            String color, String brand) throws SQLException, ClassNotFoundException {
        List<ProductDTO> productList = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT p_id, name, description, price, image_url, size, color, brand, ct_id, quantity FROM products WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sqlBuilder.append(" AND (name LIKE ? OR description LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        if (category != null && !category.isEmpty()) {
            sqlBuilder.append(" AND ct_id = ?");
            params.add(Integer.parseInt(category));
        }

        if (minPrice != null) {
            sqlBuilder.append(" AND price >= ?");
            params.add(minPrice);
        }

        if (maxPrice != null) {
            sqlBuilder.append(" AND price <= ?");
            params.add(maxPrice);
        }

        if (color != null && !color.isEmpty()) {
            sqlBuilder.append(" AND color = ?");
            params.add(color);
        }
        if (minQuantity >= 0) {
            sqlBuilder.append(" AND minQuantity = ?");
            params.add(minQuantity);
        }

        if (brand != null && !brand.isEmpty()) {
            sqlBuilder.append(" AND brand = ?");
            params.add(brand);
        }

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductDTO product = new ProductDTO();
                    product.setId(rs.getInt("p_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setImageUrl(rs.getString("image_url"));
                    product.setSize(rs.getString("size"));
                    product.setColor(rs.getString("color"));
                    product.setBrand(rs.getString("brand"));
                    product.setCt_id(rs.getInt("ct_id"));
                    productList.add(product);
                }
            }
        }
        return productList;
    }

    public boolean addProduct(ProductDTO product) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO products (name, description, price, image_url, size, color, brand, ct_id, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getImageUrl());
            ps.setString(5, product.getSize());
            ps.setString(6, product.getColor());
            ps.setString(7, product.getBrand());
            ps.setInt(8, product.getCt_id());
            ps.setInt(8, product.getQuantity());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateProduct(ProductDTO product) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, image_url = ?, size = ?, color = ?, brand = ?, ct_id = ?, quantity = ? WHERE p_id = ?";

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getImageUrl());
            ps.setString(5, product.getSize());
            ps.setString(6, product.getColor());
            ps.setString(7, product.getBrand());
            ps.setInt(8, product.getCt_id());
            ps.setInt(8, product.getQuantity());
            ps.setInt(9, product.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteProduct(int productId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM products WHERE p_id = ?";

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<String> getAllBrands() throws SQLException, ClassNotFoundException {
        List<String> brands = new ArrayList<>();
        String sql = "SELECT DISTINCT brand FROM products ORDER BY brand";

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                brands.add(rs.getString("brand"));
            }
        }
        return brands;
    }

    public List<String> getAllColors() throws SQLException, ClassNotFoundException {
        List<String> colors = new ArrayList<>();
        String sql = "SELECT DISTINCT color FROM products ORDER BY color";

        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                colors.add(rs.getString("color"));
            }
        }
        return colors;
    }
}
