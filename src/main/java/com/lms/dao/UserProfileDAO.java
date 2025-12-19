package com.lms.dao;

import com.lms.model.UserProfile;
import com.lms.util.DBUtil;
import java.sql.*;

/**
 * Data Access Object cho bảng user_profiles
 * Xử lý các thao tác CRUD với database
 */
public class UserProfileDAO {
    
    /**
     * Tìm profile theo user_id
     * @param userId ID của user
     * @return UserProfile object nếu tìm thấy, null nếu không tìm thấy
     */
    public UserProfile findByUserId(int userId) {
        String sql = "SELECT * FROM user_profiles WHERE user_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserProfile(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm profile theo user_id: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Tạo profile mới
     * @param profile UserProfile object cần tạo
     * @return ID của profile vừa tạo, -1 nếu lỗi
     */
    public int create(UserProfile profile) {
        String sql = "INSERT INTO user_profiles (user_id, ho_ten, chuyen_nganh, nam_hoc) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, profile.getUserId());
            pstmt.setString(2, profile.getHoTen());
            pstmt.setString(3, profile.getChuyenNganh());
            pstmt.setString(4, profile.getNamHoc());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo profile: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Cập nhật profile
     * @param profile UserProfile object cần cập nhật
     * @return true nếu cập nhật thành công, false nếu lỗi
     */
    public boolean update(UserProfile profile) {
        String sql = "UPDATE user_profiles SET ho_ten = ?, chuyen_nganh = ?, nam_hoc = ? WHERE user_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, profile.getHoTen());
            pstmt.setString(2, profile.getChuyenNganh());
            pstmt.setString(3, profile.getNamHoc());
            pstmt.setInt(4, profile.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật profile: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Tạo hoặc cập nhật profile
     * Nếu chưa có thì create, có rồi thì update
     * @param profile UserProfile object
     * @return true nếu thành công, false nếu lỗi
     */
    public boolean createOrUpdate(UserProfile profile) {
        // Kiểm tra profile đã tồn tại chưa
        UserProfile existing = findByUserId(profile.getUserId());
        
        if (existing == null) {
            // Chưa có, tạo mới
            return create(profile) > 0;
        } else {
            // Đã có, cập nhật
            profile.setId(existing.getId());
            return update(profile);
        }
    }
    
    /**
     * Map ResultSet thành UserProfile object
     * @param rs ResultSet từ database
     * @return UserProfile object
     * @throws SQLException nếu có lỗi khi đọc dữ liệu
     */
    private UserProfile mapResultSetToUserProfile(ResultSet rs) throws SQLException {
        UserProfile profile = new UserProfile();
        profile.setId(rs.getInt("id"));
        profile.setUserId(rs.getInt("user_id"));
        profile.setHoTen(rs.getString("ho_ten"));
        profile.setChuyenNganh(rs.getString("chuyen_nganh"));
        profile.setNamHoc(rs.getString("nam_hoc"));
        profile.setUpdatedAt(rs.getTimestamp("updated_at"));
        return profile;
    }
}

