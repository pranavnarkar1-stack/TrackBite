package com.jaha;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/java_project";
            String username = "root";
            String password = "pranav";

            Connection con = DriverManager.getConnection(url, username, password);

            System.out.println("✅ MySQL connected successfully!");
            con.close();
        } catch (Exception e) {
            System.out.println("❌ MySQL connection failed");
            e.printStackTrace();
        }
    }
}
