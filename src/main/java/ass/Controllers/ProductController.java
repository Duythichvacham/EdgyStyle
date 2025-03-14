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
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductController extends HttpServlet {

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
                String brand = request.getParameter("brand");
                
                if (request.getParameter("minPrice") != null && !request.getParameter("minPrice").isEmpty()) {
                    minPrice = Double.parseDouble(request.getParameter("minPrice"));
                }
                
                if (request.getParameter("maxPrice") != null && !request.getParameter("maxPrice").isEmpty()) {
                    maxPrice = Double.parseDouble(request.getParameter("maxPrice"));
                }
                
                List<ProductDTO> products = productDAO.searchProducts(keyword, category, minPrice, maxPrice, minQuantity, color, brand);
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
                if (request.getSession().getAttribute("role") == null || 
                    !request.getSession().getAttribute("role").equals("Admin")) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                
                List<CategoryDTO> categories = categoryDAO.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("addProduct.jsp").forward(request, response);
            } else if ("AddProduct".equals(action)) {
                // Only admin can access this
                if (request.getSession().getAttribute("role") == null || 
                    !request.getSession().getAttribute("role").equals("Admin")) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                String imageUrl = request.getParameter("imageUrl");
                String size = request.getParameter("size");
                String color = request.getParameter("color");
                String brand = request.getParameter("brand");
                int ct_Id = Integer.parseInt(request.getParameter("ct_id"));
                
                ProductDTO product = new ProductDTO();
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setImageUrl(imageUrl);
                product.setSize(size);
                product.setColor(color);
                product.setBrand(brand);
                product.setCt_id(ct_Id);
                
                boolean success = productDAO.addProduct(product);
                
                if (success) {
                    request.setAttribute("SUCCESS", "Product added successfully.");
                    request.getRequestDispatcher("ProductController?action=ViewProducts").forward(request, response);
                } else {
                    request.setAttribute("ERROR", "Failed to add product.");
                    request.getRequestDispatcher("addProduct.jsp").forward(request, response);
                }
            } else if ("EditProductForm".equals(action)) {
                // Only admin can access this
                if (request.getSession().getAttribute("role") == null || 
                    !request.getSession().getAttribute("role").equals("Admin")) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                
                int productId = Integer.parseInt(request.getParameter("productId"));
                ProductDTO product = productDAO.getProductById(productId);
                List<CategoryDTO> categories = categoryDAO.getAllCategories();
                
                request.setAttribute("product", product);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("editProduct.jsp").forward(request, response);
            } else if ("UpdateProduct".equals(action)) {
                // Only admin can access this
                if (request.getSession().getAttribute("role") == null || 
                    !request.getSession().getAttribute("role").equals("Admin")) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                
                int productId = Integer.parseInt(request.getParameter("productId"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                String imageUrl = request.getParameter("imageUrl");
                String size = request.getParameter("size");
                String color = request.getParameter("color");
                String brand = request.getParameter("brand");
                String categoryId = request.getParameter("categoryId");
                
                ProductDTO product = new ProductDTO();
                product.setId(productId);
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setImageUrl(imageUrl);
                product.setSize(size);
                product.setColor(color);
                product.setBrand(brand);
                product.setCt_id(productId);
                
                boolean success = productDAO.updateProduct(product);
                
                if (success) {
                    request.setAttribute("SUCCESS", "Product updated successfully.");
                    request.getRequestDispatcher("ProductController?action=ViewProducts").forward(request, response);
                } else {
                    request.setAttribute("ERROR", "Failed to update product.");
                    request.getRequestDispatcher("editProduct.jsp").forward(request, response);
                }
            } else if ("DeleteProduct".equals(action)) {
                // Only admin can access this
                if (request.getSession().getAttribute("role") == null || 
                    !request.getSession().getAttribute("role").equals("Admin")) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                
                int productId = Integer.parseInt(request.getParameter("productId"));
                boolean success = productDAO.deleteProduct(productId);
                
                if (success) {
                    request.setAttribute("SUCCESS", "Product deleted successfully.");
                } else {
                    request.setAttribute("ERROR", "Failed to delete product.");
                }
                
                request.getRequestDispatcher("ProductController?action=ViewProducts").forward(request, response);
            } else {
                request.getRequestDispatcher("index.jsp").forward(request, response);
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
