package com.jaha;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ✅ Database configuration
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/java_project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pranav";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ Get form inputs
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // ✅ Validate inputs
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            response.sendRedirect("login_register.html?msg=Username%20or%20password%20cannot%20be%20empty");
            return;
        }

        try {
            // ✅ Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Connect to database
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {

                // ✅ Check if username already exists
                String checkSql = "SELECT username FROM java_userdata WHERE username = ?";
                try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                    checkPs.setString(1, username);
                    ResultSet rs = checkPs.executeQuery();
                    if (rs.next()) {
                        response.sendRedirect("login_register.html?msg=Username%20already%20exists");
                        return;
                    }
                }

                // ✅ Insert new user
                String insertSql = "INSERT INTO java_userdata (username, pass) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setString(1, username);
                    ps.setString(2, password); // For testing: plain-text password
                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        response.sendRedirect("login_register.html?msg=Registration%20successful");
                    } else {
                        response.sendRedirect("login_register.html?msg=Registration%20failed");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // shows real error in Tomcat console
            response.sendRedirect("login_register.html?msg=Error%20during%20registration");
        }
    }
}
