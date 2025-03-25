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

import db.utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public class ProductDAO {

    public List<ProductDTO> getAllProducts() throws SQLException, ClassNotFoundException {
       List<ProductDTO> productList = new ArrayList<>();
        String sql = "SELECT p_id, name, description, price, size, color, ct_id, stock_quantity FROM products";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setId(rs.getInt("p_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setSize(rs.getString("size"));
                product.setColor(rs.getString("color"));
                product.setCt_id(rs.getInt("ct_id"));
                product.setStock_quantity(rs.getInt("stock_quantity"));
                productList.add(product);
            }
        }
        return productList;
    }

    // Lấy sản phẩm theo ID (không bao gồm URL ảnh)
    public ProductDTO getProductById(int productId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT p_id, name, description, price, size, color, ct_id, stock_quantity FROM products WHERE p_id = ?";
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
                    product.setSize(rs.getString("size"));
                    product.setColor(rs.getString("color"));
                    product.setCt_id(rs.getInt("ct_id"));
                    product.setStock_quantity(rs.getInt("stock_quantity"));
                }
            }
        }
        return product;
    }

    // Tìm kiếm sản phẩm (không bao gồm URL ảnh)
    public List<ProductDTO> searchProducts(String keyword, String category, Double minPrice, Double maxPrice, int minQuantity,
            String color) throws SQLException, ClassNotFoundException {
        List<ProductDTO> productList = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT p_id, name, description, price, size, color, ct_id, stock_quantity FROM products WHERE 1=1");

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
            sqlBuilder.append(" AND stock_quantity >= ?");
            params.add(minQuantity);
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
                    product.setSize(rs.getString("size"));
                    product.setColor(rs.getString("color"));
                    product.setCt_id(rs.getInt("ct_id"));
                    product.setStock_quantity(rs.getInt("stock_quantity"));
                    productList.add(product);
                }
            }
        }
        return productList;
    }

    // Thêm phương thức để lấy danh sách URL ảnh cho một sản phẩm
    public List<ProductImgUrlDTO> getProductImages(int productId) throws SQLException, ClassNotFoundException {
        List<ProductImgUrlDTO> images = new ArrayList<>();
        String sql = "SELECT p_id, img_url FROM product_img_url WHERE p_id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductImgUrlDTO image = new ProductImgUrlDTO();
                    image.setP_id(rs.getInt("p_id"));
                    image.setImg_url(rs.getString("img_url"));
                    images.add(image);
                }
            }
        }
        return images;
    }

    public boolean addProductWithImages(ProductDTO product, List<String> imgUrls) throws SQLException, ClassNotFoundException {
    try (Connection conn = DBUtils.getConnection()) {
        conn.setAutoCommit(false);
        int productId = addProduct(product, conn);
        if (productId <= 0) {
            conn.rollback();
            conn.close();
            throw new SQLException("Failed to add product. Product ID: " + productId);
        }

        boolean imagesAdded = addProductImgUrl(productId, imgUrls, conn);
        if (!imagesAdded) {
            conn.rollback();
            conn.close();
            throw new SQLException("Failed to add product images. Number of images: " + imgUrls.size());
        }

        conn.commit();
        conn.close();
        return true;
    } catch (SQLException | ClassNotFoundException e) {
        System.err.println("Error in addProductWithImages: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}

    public int addProduct(ProductDTO product, Connection conn) throws SQLException, ClassNotFoundException {
    String getSeqSql = "SELECT NEXT VALUE FOR product_id_seq";
    int productId;

    try (PreparedStatement psSeq = conn.prepareStatement(getSeqSql);
         ResultSet rs = psSeq.executeQuery()) {
        if (rs.next()) {
            productId = rs.getInt(1);
        } else {
            throw new SQLException("Không thể lấy giá trị từ SEQUENCE product_id_seq.");
        }
    }

    String sql = "INSERT INTO products (p_id, name, description, price, size, color, ct_id, stock_quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, productId);
        ps.setString(2, product.getName());
        ps.setString(3, product.getDescription());
        ps.setDouble(4, product.getPrice());
        ps.setString(5, product.getSize());
        ps.setString(6, product.getColor());
        ps.setInt(7, product.getCt_id());
        ps.setInt(8, product.getStock_quantity());

        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Thêm sản phẩm thất bại: Không có bản ghi nào được thêm.");
        }
        System.out.println("Inserted Product ID: " + productId); // Log để debug
        return productId;
    } catch (SQLException e) {
        System.err.println("Error in addProduct: " + e.getMessage());
        throw e;
    }

}
    // thêm list img vào table product_img_url

    public boolean addProductImgUrl(int p_id, List<String> img_url_list, Connection conn) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO product_img_url (p_id,img_url) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String url : img_url_list) {
                ps.setInt(1, p_id);
                ps.setString(2, url);
                ps.addBatch(); // Thêm vào batch
            }

            int[] result = ps.executeBatch(); // Thực thi batch insert

            return result.length == img_url_list.size(); // Trả về true nếu chèn đủ số lượng
        }
    }

    public List<ProductImgUrlDTO> updateProductWithImages(ProductDTO product, List<String> newImgUrls) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBUtils.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Lấy danh sách ảnh cũ để xóa file vật lý sau này
            List<ProductImgUrlDTO> oldImages = getProductImages(product.getId());

            // Cập nhật thông tin sản phẩm
            String updateProductSql = "UPDATE products SET name = ?, description = ?, price = ?, size = ?, color = ?, ct_id = ?, stock_quantity = ? WHERE p_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateProductSql)) {
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setDouble(3, product.getPrice());
                ps.setString(4, product.getSize());
                ps.setString(5, product.getColor());
                ps.setInt(6, product.getCt_id());
                ps.setInt(7, product.getStock_quantity());
                ps.setInt(8, product.getId());

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Cập nhật sản phẩm thất bại.");
                }
            }

            // Xóa các ảnh cũ trong DB
            String deleteImagesSql = "DELETE FROM product_img_url WHERE p_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteImagesSql)) {
                ps.setInt(1, product.getId());
                ps.executeUpdate();
            }

            // Thêm các ảnh mới
            String insertImageSql = "INSERT INTO product_img_url (p_id, img_url) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertImageSql)) {
                for (String imgUrl : newImgUrls) {
                    ps.setInt(1, product.getId());
                    ps.setString(2, imgUrl);
                    ps.executeUpdate();
                }
            }

            conn.commit(); // Lưu thay đổi nếu thành công

            // Trả về danh sách ảnh cũ để xóa file vật lý
             // Lưu danh sách ảnh cũ để xóa file vật lý
            return oldImages;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
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
