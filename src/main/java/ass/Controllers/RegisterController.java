/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass.Controllers;

import ass.Users.UserDAO;
import ass.Users.UserDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nttu2
 */
public class RegisterController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");

        try {
            UserDAO userDAO = new UserDAO();

            if ("register".equals(action)) {
                String username = request.getParameter("username");
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String confirmPassword = request.getParameter("confirmPassword");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");

                // Validate input
                if (username == null || username.trim().isEmpty()
                        || fullName == null || fullName.trim().isEmpty()
                        || email == null || email.trim().isEmpty()
                        || password == null || password.trim().isEmpty()
                        || confirmPassword == null || confirmPassword.trim().isEmpty()) {

                    request.setAttribute("ERROR", "All fields are required");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    request.setAttribute("ERROR", "Passwords do not match");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }

                // Check if username or email already exists
                if (userDAO.isUsernameExists(username)) {
                    request.setAttribute("ERROR", "Username already exists");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }

                if (userDAO.isEmailExists(email)) {
                    request.setAttribute("ERROR", "Email already exists");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }

                // Create new user
                UserDTO user = new UserDTO();
                user.setUsername(username);
                user.setFull_name(fullName);
                user.setEmail(email);
                user.setPassword(password);
                user.setPhone(phone);
                user.setAddress(address);
                user.setRole("User");

                boolean success = userDAO.registerUser(user);

                if (success) {
                    request.setAttribute("SUCCESS", "Registration successful. Please login.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                } else {
                    request.setAttribute("ERROR", "Registration failed. Please try again.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("ERROR", "Error in registration: " + e.getMessage());
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
        return "Register Controller";
    }
}
