package ass.Controllers;

import ass.Categories.CategoryDAO;
import ass.Products.ProductDAO;
import ass.Products.ProductDTO;
import ass.Products.ProductImgUrlDTO;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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

    // Hàm phụ để load danh sách sản phẩm
    private void loadProducts(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        ProductDAO productDAO = new ProductDAO();
        List<ProductDTO> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        try {
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            // Nếu không có action hoặc action rỗng, mặc định hiển thị danh sách sản phẩm
            if (action == null || action.isEmpty()) {
                loadProducts(request);
                request.getRequestDispatcher("productManager.jsp").forward(request, response);
                return;
            }

            if ("ViewProducts".equals(action)) {
                loadProducts(request);
                request.getRequestDispatcher("productManager.jsp").forward(request, response);
            } else if ("AddProduct".equals(action)) {
                // cần test session and role
                try {
                    String name = request.getParameter("name");
                    String description = request.getParameter("description");
                    double price = Double.parseDouble(request.getParameter("price"));
                    String size = request.getParameter("size");
                    String color = request.getParameter("color");
                    int ct_id = Integer.parseInt(request.getParameter("ct_id"));
                    int stock_quantity = Integer.parseInt(request.getParameter("stock_quantity"));

                    ProductDTO product = new ProductDTO();
                    product.setName(name);
                    product.setDescription(description);
                    product.setPrice(price);
                    product.setSize(size);
                    product.setColor(color);
                    product.setCt_id(ct_id);
                    product.setStock_quantity(stock_quantity);

                    List<String> imgUrls = new ArrayList<>();
                    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                    File uploadDir = new File(uploadPath);
                    System.out.println("Upload path: " + uploadPath);
                    if (!uploadDir.exists()) {
                        boolean created = uploadDir.mkdirs(); // Sử dụng mkdirs() thay cho mkdir()
                        System.out.println("Created upload directory: " + created);
                        if (!created) {
                            throw new IOException("Failed to create upload directory: " + uploadPath);
                        }
                    }

                    for (Part part : request.getParts()) {
                        if (part.getName().equals("images") && part.getSize() > 0) {
                            String fileName = UUID.randomUUID().toString() + "_" + part.getSubmittedFileName();
                            String filePath = uploadPath + File.separator + fileName;
                            System.out.println("Writing file to: " + filePath);
                            part.write(filePath);
                            File uploadedFile = new File(filePath);
                            if (!uploadedFile.exists()) {
                                throw new IOException("Failed to write file: " + filePath);
                            }
                            System.out.println("File written successfully: " + uploadedFile.exists());
                            String relativePath = UPLOAD_DIR + "/" + fileName;
                            imgUrls.add(relativePath);
                        }
                    }

                    if (imgUrls.isEmpty()) {
                        request.setAttribute("ERROR", "Vui lòng chọn ít nhất một file ảnh.");
                        loadProducts(request);
                        request.getRequestDispatcher("productManager.jsp").forward(request, response);
                        return;
                    }

                    boolean success = productDAO.addProductWithImages(product, imgUrls);
                    if (success) {
                        request.setAttribute("SUCCESS", "Product added successfully.");
                    } else {
                        request.setAttribute("ERROR", "Failed to add product: Unknown error.");
                    }
                    loadProducts(request);
                    request.getRequestDispatcher("productManager.jsp").forward(request, response);
                } catch (Exception e) {
                    request.setAttribute("ERROR", "Error adding product: " + e.getMessage());
                    try {
                        loadProducts(request);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    request.getRequestDispatcher("productManager.jsp").forward(request, response);
                }
            } else if ("DeleteProduct".equals(action)) {
                if (request.getSession().getAttribute("role") == null
                        || !request.getSession().getAttribute("role").equals("Admin")) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                int productId = Integer.parseInt(request.getParameter("productId"));
                List<ProductImgUrlDTO> oldImages = productDAO.getProductImages(productId);
                boolean success = productDAO.deleteProduct(productId);

                if (success) {
                    for (ProductImgUrlDTO oldImage : oldImages) {
                        new File(getServletContext().getRealPath("") + File.separator + oldImage.getImg_url()).delete();
                    }
                    request.setAttribute("SUCCESS", "Product deleted successfully.");
                } else {
                    request.setAttribute("ERROR", "Failed to delete product.");
                }
                loadProducts(request); // Load lại danh sách sản phẩm
                request.getRequestDispatcher("productManager.jsp").forward(request, response);

            } else if ("ViewProductDetails".equals(action)) {
                // Giữ nguyên để điều hướng tới trang chỉnh sửa sau này
                int productId = Integer.parseInt(request.getParameter("productId"));
                ProductDTO product = productDAO.getProductById(productId);
                request.setAttribute("product", product);
                request.getRequestDispatcher("productDetails.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("ERROR", "Error in processing product: " + e.getMessage());
            try {
                loadProducts(request); // Load lại danh sách sản phẩm ngay cả khi có lỗi
            } catch (Exception ex) {
                e.printStackTrace();
            }
            request.getRequestDispatcher("productManager.jsp").forward(request, response);
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
