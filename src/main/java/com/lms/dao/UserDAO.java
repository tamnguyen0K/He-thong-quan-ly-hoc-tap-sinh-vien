package com.lms.dao;

import com.lms.model.User;
import com.lms.util.DBUtil;
import java.sql.*;

/**
 * Data Access Object cho bảng users
 * Xử lý các thao tác CRUD với database
 */
public class UserDAO {
    
    /**
     * Tìm user theo email
     * @param email Email cần tìm
     * @return User object nếu tìm thấy, null nếu không tìm thấy
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm user theo email: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Tạo user mới
     * @param user User object cần tạo
     * @return ID của user vừa tạo, -1 nếu lỗi
     */
    public int create(User user) {
        String sql = "INSERT INTO users (email, password_hash, role) VALUES (?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getRole() != null ? user.getRole() : "student");
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo user: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Validate login - kiểm tra email và password hash
     * @param email Email của user
     * @param passwordHash Password hash cần kiểm tra
     * @return User object nếu hợp lệ, null nếu không hợp lệ
     */
    public User validateLogin(String email, String passwordHash) {
        // Tìm user theo email
        User user = findByEmail(email);
        
        // Nếu tìm thấy user và password hash khớp
        if (user != null && user.getPasswordHash().equals(passwordHash)) {
            return user;
        }
        
        return null;
    }
    
    /**
     * Lấy user theo ID
     * @param id ID của user
     * @return User object nếu tìm thấy, null nếu không tìm thấy
     */
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm user theo ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Map ResultSet thành User object
     * @param rs ResultSet từ database
     * @return User object
     * @throws SQLException nếu có lỗi khi đọc dữ liệu
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}

