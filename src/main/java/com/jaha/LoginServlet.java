package com.jaha;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/java_project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pranav";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            response.sendRedirect("login_register.html?msg=Username%20or%20password%20cannot%20be%20empty");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM java_userdata WHERE username=? AND pass=?")) {

                ps.setString(1, username);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        response.sendRedirect("after_login.html?user=" + username);
                    } else {
                        response.sendRedirect("login_register.html?msg=Invalid%20username%20or%20password");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // shows actual reason in console
            response.sendRedirect("login_register.html?msg=Error%20during%20login");
        }
    }
}
