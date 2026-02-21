package com.jaha;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12817692";
            String username = "sql12817692";
            String password = "UIbSXRrAMi";

            Connection con = DriverManager.getConnection(url, username, password);

            System.out.println("✅ MySQL connected successfully!");
            con.close();
        } catch (Exception e) {
            System.out.println("❌ MySQL connection failed");
            e.printStackTrace();
        }
    }
}
