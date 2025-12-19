package com.lms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class để quản lý kết nối database
 * Cung cấp method getConnection() để lấy Connection đến MySQL
 * 
 * HƯỚNG DẪN: 
 * 1. Copy file này thành DBUtil.java
 * 2. Điền thông tin kết nối database của bạn
 * 3. KHÔNG commit file DBUtil.java lên git (đã được thêm vào .gitignore)
 */
public class DBUtil {

    // Thông tin kết nối database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quan_ly_hoc_tap?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8&useUnicode=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "YOUR_PASSWORD_HERE"; // Điền password MySQL của bạn vào đây

    /**
     * Lấy Connection đến MySQL database
     *
     * @return Connection object
     * @throws SQLException nếu không kết nối được
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tạo và trả về connection
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver không tìm thấy", e);
        }
    }

    /**
     * Đóng connection (helper method)
     *
     * @param conn Connection cần đóng
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng connection: " + e.getMessage());
            }
        }
    }
}

