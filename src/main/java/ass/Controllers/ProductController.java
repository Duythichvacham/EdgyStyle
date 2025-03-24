/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass.Controllers;

/**
 *
 * @author nttu2
 */
import ass.Categories.CategoryDAO;
import ass.Categories.CategoryDTO;
import ass.Products.ProductDAO;
import ass.Products.ProductDTO;
import ass.Products.ProductImgUrlDTO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

public class ProductController extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");

        try {
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            if ("ViewProducts".equals(action)) {
                List<ProductDTO> products = productDAO.getAllProducts();
                request.setAttribute("products", products);
                request.getRequestDispatcher("productList.jsp").forward(request, response);
            } else if ("SearchProducts".equals(action)) {
                String keyword = request.getParameter("keyword");
                String category = request.getParameter("category");
                Double minPrice = null;
                Double maxPrice = null;
                int minQuantity = 0;
                String color = request.getParameter("color");

                if (request.getParameter("minPrice") != null && !request.getParameter("minPrice").isEmpty()) {
                    minPrice = Double.parseDouble(request.getParameter("minPrice"));
                }

                if (request.getParameter("maxPrice") != null && !request.getParameter("maxPrice").isEmpty()) {
                    maxPrice = Double.parseDouble(request.getParameter("maxPrice"));
                }

                List<ProductDTO> products = productDAO.searchProducts(keyword, category, minPrice, maxPrice, minQuantity, color);
                request.setAttribute("products", products);
                request.setAttribute("keyword", keyword);
                request.getRequestDispatcher("productList.jsp").forward(request, response);
            } else if ("ViewProductDetails".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                ProductDTO product = productDAO.getProductById(productId);
                request.setAttribute("product", product);
                request.getRequestDispatcher("productDetails.jsp").forward(request, response);
            } else if ("AddProductForm".equals(action)) {
                // Only admin can access this
                if (request.getSession().getAttribute("role") == null
                        || !request.getSession().getAttribute("role").equals("Admin")) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                List<CategoryDTO> categories = categoryDAO.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("addProduct.jsp").forward(request, response);
            } else if ("AddProduct".equals(action)) {
                // admin mới cho add
                if (request.getSession().getAttribute("role") == null
                        || !request.getSession().getAttribute("role").equals("Admin")) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                // Lấy thông tin sản phẩm từ request
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                String size = request.getParameter("size");
                String color = request.getParameter("color");
                int ct_id = Integer.parseInt(request.getParameter("ct_id"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                // Tạo đối tượng ProductDTO
                ProductDTO product = new ProductDTO();
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setSize(size);
                product.setColor(color);
                product.setCt_id(ct_id);
                product.setQuantity(quantity);

                // Xử lý file ảnh
                List<String> imgUrls = new ArrayList<>();

                // Đường dẫn thư mục lưu file trên server
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir(); // Tạo thư mục nếu chưa tồn tại
                }

                // Lấy danh sách file từ request
                for (Part part : request.getParts()) {
                    if (part.getName().equals("images") && part.getSize() > 0) {
                        // Tạo tên file duy nhất để tránh trùng lặp với UUID
                        String fileName = UUID.randomUUID().toString() + "_" + part.getSubmittedFileName();
                        String filePath = uploadPath + File.separator + fileName;
                        part.write(filePath); // Lưu file vào thư mục

                        // Tạo đường dẫn tương đối để lưu vào DB
                        String relativePath = UPLOAD_DIR + "/" + fileName;
                        imgUrls.add(relativePath);
                    }
                }

                // Kiểm tra xem có file nào được tải lên không
                if (imgUrls.isEmpty()) {
                    request.setAttribute("ERROR", "Vui lòng chọn ít nhất một file ảnh.");
                    request.getRequestDispatcher("addProduct.jsp").forward(request, response);
                    return;
                }

                // Gọi phương thức addProductWithImages từ ProductDAO
                boolean success = productDAO.addProductWithImages(product, imgUrls);

                // Xử lý kết quả
                if (success) {
                    request.setAttribute("SUCCESS", "Product added successfully.");
                    request.getRequestDispatcher("ProductController?action=ViewProducts").forward(request, response);
                } else {
                    request.setAttribute("ERROR", "Failed to add product.");
                    request.getRequestDispatcher("addProduct.jsp").forward(request, response);
                }

            } else if ("UpdateProduct".equals(action)) {
                // Only admin can access this
                if (request.getSession().getAttribute("role") == null
                        || !request.getSession().getAttribute("role").equals("Admin")) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                // Lấy thông tin sản phẩm từ request
                int productId = Integer.parseInt(request.getParameter("productId"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                String size = request.getParameter("size");
                String color = request.getParameter("color");
                int categoryId = Integer.parseInt(request.getParameter("ct_id"));
                int quantity = Integer.parseInt(request.getParameter("quantity")); // Thêm quantity

                // Tạo đối tượng ProductDTO
                ProductDTO product = new ProductDTO();
                product.setId(productId);
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setSize(size);
                product.setColor(color);
                product.setCt_id(categoryId);
                product.setQuantity(quantity);

                // Xử lý file ảnh
                List<String> imgUrls = new ArrayList<>();
                // Đường dẫn thư mục lưu file trên server
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir(); // Tạo thư mục nếu chưa tồn tại
                }

                // Lấy danh sách file từ request
                for (Part part : request.getParts()) {
                    if (part.getName().equals("images") && part.getSize() > 0) {
                        // Tạo tên file duy nhất để tránh trùng lặp
                        String fileName = UUID.randomUUID().toString() + "_" + part.getSubmittedFileName();
                        String filePath = uploadPath + File.separator + fileName;
                        part.write(filePath); // Lưu file vào thư mục

                        // Tạo đường dẫn tương đối để lưu vào DB
                        String relativePath = UPLOAD_DIR + "/" + fileName;
                        imgUrls.add(relativePath);
                    }
                }

                // Gọi phương thức updateProductWithImages từ ProductDAO
                List<ProductImgUrlDTO> oldImages = productDAO.updateProductWithImages(product, imgUrls);

                // Xử lý kết quả
                if (oldImages != null) { // Kiểm tra xem có ảnh cũ không
                    // Xóa các file vật lý của ảnh cũ
                    for (ProductImgUrlDTO oldImage : oldImages) {
                        new File(getServletContext().getRealPath("") + File.separator + oldImage.getImg_url()).delete();
                    }
                    request.setAttribute("SUCCESS", "Product updated successfully.");
                    request.getRequestDispatcher("ProductController?action=ViewProducts").forward(request, response);
                } else {
                    // Xóa các file mới đã tải lên nếu cập nhật thất bại
                    for (String imgUrl : imgUrls) {
                        new File(getServletContext().getRealPath("") + File.separator + imgUrl).delete();
                    }
                    request.setAttribute("ERROR", "Failed to update product.");
                    request.getRequestDispatcher("editProduct.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("ERROR", "Error in processing product: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Product Controller";
    }
}
