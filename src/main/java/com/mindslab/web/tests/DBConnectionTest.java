package com.mindslab.web.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mariadb://172.30.1.5:3306/mindslab?characterEncoding=UTF-8&serverTimezone=UTC";
        String user = "mindslab";
        String password = "mindslab";
        String sql = "show databases";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
