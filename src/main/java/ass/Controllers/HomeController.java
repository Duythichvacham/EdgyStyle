/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass.Controllers;

/**
 *
 * @author LENOVO
 */
import ass.Categories.CategoryDAO;
import ass.Categories.CategoryDTO;
import ass.Products.ProductDAO;
import ass.Products.ProductDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            ProductDAO productDAO = new ProductDAO();
            
            List<CategoryDTO> categories = categoryDAO.getAllCategories();
            List<ProductDTO> featuredProducts = productDAO.getAllProducts(); // In a real app, you'd modify this to get only featured products
            
            request.setAttribute("categories", categories);
            request.setAttribute("featuredProducts", featuredProducts);
            
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("ERROR", "Error loading home page: " + e.getMessage());
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
        return "Home Controller";
    }
}
