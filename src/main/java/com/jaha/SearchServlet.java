package com.jaha;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashSet; // Import for tracking unique names
import java.util.Set;    // Import for Set interface

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/searching")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/java_project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pranav";

    // Helper for formatting doubles to two decimal places (e.g., 0.80)
    private static final DecimalFormat DF = new DecimalFormat("0.00"); 

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String searchQuery = request.getParameter("query");
        
        // --- 1. Unique Name Tracker ---
        Set<String> displayedFoods = new HashSet<>();

        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            // This case handles the empty search query which is better handled 
            // by the client-side JavaScript, but kept for robustness.
            out.println("<p class='no-result'>Please enter a food to search.</p>");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {

                // Query to retrieve all nutritional data
                String sql = "SELECT name, calories, protein, fats, carbohydrates FROM foods WHERE name LIKE ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + searchQuery + "%");
                ResultSet rs = ps.executeQuery();

                boolean found = false;
                
                while (rs.next()) {
                    // Retrieve columns
                    String foodName = rs.getString("name");
                    
                    // --- 2. Check for Duplicates (Crucial Fix) ---
                    // If this food name has already been displayed, skip this iteration.
                    if (displayedFoods.contains(foodName.toLowerCase())) {
                        continue; 
                    }
                    
                    displayedFoods.add(foodName.toLowerCase());
                    found = true;
                    
                    int cal = rs.getInt("calories");
                    double protein = rs.getDouble("protein");
                    double fats = rs.getDouble("fats");
                    double carbs = rs.getDouble("carbohydrates");
                    
                    // Format foodName to start with a capital letter for display
                    String displayFoodName = foodName.substring(0, 1).toUpperCase() + foodName.substring(1).toLowerCase();

                    // --- 3. Output as a Card (Ensuring all fields are present) ---
                    out.println("<div class='food-card'>");
                    out.println("<h3>" + displayFoodName + "</h3>");
                    out.println("<p>Calories: " + cal + " kcal</p>");
                    out.println("<p>Protein: " + DF.format(protein) + " g</p>");
                    out.println("<p>Fats: " + DF.format(fats) + " g</p>");
                    out.println("<p>Carbohydrates: " + DF.format(carbs) + " g</p>");
                    
                    // Add button with data attributes for JavaScript
                    out.println("<button data-name='" + foodName + "' data-calories='" + cal + "'>Add to Meal</button>");
                    out.println("</div>");
                }

                if (!found) {
                    out.println("<p class='no-result'>No results found in foods table!</p>");
                }
            }
        } catch (Exception e) {
            out.println("<p class='no-result'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }
}