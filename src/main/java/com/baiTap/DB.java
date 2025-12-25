package com.baiTap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private String username = "root";
    private String password = "";
    private String database = "QLSieuThi";
    private String url = "jdbc:mariadb://localhost:3306/" + database;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public String get(String attribute) {
        switch (attribute) {
            case "username": return username;
            case "password": return password;
            case "url": return url;
            default: return null;
        }
    }

    public void test() {
        try (Connection con = getConnection()) {
            System.out.println("Kết nối thành công!");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Kết nối thất bại!");
        }
    }
}
