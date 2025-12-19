package com.lms.dao;

import com.lms.model.Schedule;
import com.lms.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho bảng schedules
 * Xử lý các thao tác CRUD với database
 */
public class ScheduleDAO {
    
    /**
     * Lấy tất cả lịch học của user, sắp xếp theo thứ và thời gian
     * @param userId ID của user
     * @return Danh sách Schedule
     */
    public List<Schedule> findAllByUserId(int userId) {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedules WHERE user_id = ? ORDER BY " +
                     "FIELD(thu_trong_tuan, 'Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7', 'Chủ nhật'), " +
                     "thoi_gian_bat_dau";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapResultSetToSchedule(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách lịch học: " + e.getMessage());
        }
        
        return schedules;
    }
    
    /**
     * Tìm schedule theo ID
     * @param id ID của schedule
     * @return Schedule object nếu tìm thấy, null nếu không tìm thấy
     */
    public Schedule findById(int id) {
        String sql = "SELECT * FROM schedules WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSchedule(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm schedule theo ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Tạo schedule mới
     * @param schedule Schedule object cần tạo
     * @return ID của schedule vừa tạo, -1 nếu lỗi
     */
    public int create(Schedule schedule) {
        String sql = "INSERT INTO schedules (user_id, ten_mon, thu_trong_tuan, thoi_gian_bat_dau, " +
                     "thoi_gian_ket_thuc, phong_hoc, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, schedule.getUserId());
            pstmt.setString(2, schedule.getTenMon());
            pstmt.setString(3, schedule.getThuTrongTuan());
            pstmt.setTime(4, schedule.getThoiGianBatDau());
            pstmt.setTime(5, schedule.getThoiGianKetThuc());
            pstmt.setString(6, schedule.getPhongHoc());
            pstmt.setString(7, schedule.getGhiChu());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo schedule: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Cập nhật schedule
     * @param schedule Schedule object cần cập nhật
     * @return true nếu cập nhật thành công, false nếu lỗi
     */
    public boolean update(Schedule schedule) {
        String sql = "UPDATE schedules SET ten_mon = ?, thu_trong_tuan = ?, thoi_gian_bat_dau = ?, " +
                     "thoi_gian_ket_thuc = ?, phong_hoc = ?, ghi_chu = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, schedule.getTenMon());
            pstmt.setString(2, schedule.getThuTrongTuan());
            pstmt.setTime(3, schedule.getThoiGianBatDau());
            pstmt.setTime(4, schedule.getThoiGianKetThuc());
            pstmt.setString(5, schedule.getPhongHoc());
            pstmt.setString(6, schedule.getGhiChu());
            pstmt.setInt(7, schedule.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật schedule: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Xóa schedule
     * @param id ID của schedule cần xóa
     * @return true nếu xóa thành công, false nếu lỗi
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM schedules WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa schedule: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Kiểm tra trùng lịch
     * @param userId ID của user
     * @param thu Thứ trong tuần
     * @param batDau Thời gian bắt đầu
     * @param ketThuc Thời gian kết thúc
     * @param excludeId ID của schedule cần loại trừ (dùng khi update), -1 nếu không loại trừ
     * @return true nếu trùng lịch, false nếu không trùng
     */
    public boolean checkConflict(int userId, String thu, Time batDau, Time ketThuc, int excludeId) {
        String sql = "SELECT COUNT(*) FROM schedules WHERE user_id = ? AND thu_trong_tuan = ? " +
                     "AND ((thoi_gian_bat_dau <= ? AND thoi_gian_ket_thuc > ?) OR " +
                     "(thoi_gian_bat_dau < ? AND thoi_gian_ket_thuc >= ?) OR " +
                     "(thoi_gian_bat_dau >= ? AND thoi_gian_ket_thuc <= ?))";
        
        if (excludeId > 0) {
            sql += " AND id != ?";
        }
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int paramIndex = 1;
            pstmt.setInt(paramIndex++, userId);
            pstmt.setString(paramIndex++, thu);
            pstmt.setTime(paramIndex++, batDau);
            pstmt.setTime(paramIndex++, batDau);
            pstmt.setTime(paramIndex++, ketThuc);
            pstmt.setTime(paramIndex++, ketThuc);
            pstmt.setTime(paramIndex++, batDau);
            pstmt.setTime(paramIndex++, ketThuc);
            
            if (excludeId > 0) {
                pstmt.setInt(paramIndex++, excludeId);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra trùng lịch: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Map ResultSet thành Schedule object
     * @param rs ResultSet từ database
     * @return Schedule object
     * @throws SQLException nếu có lỗi khi đọc dữ liệu
     */
    private Schedule mapResultSetToSchedule(ResultSet rs) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setId(rs.getInt("id"));
        schedule.setUserId(rs.getInt("user_id"));
        schedule.setTenMon(rs.getString("ten_mon"));
        schedule.setThuTrongTuan(rs.getString("thu_trong_tuan"));
        schedule.setThoiGianBatDau(rs.getTime("thoi_gian_bat_dau"));
        schedule.setThoiGianKetThuc(rs.getTime("thoi_gian_ket_thuc"));
        schedule.setPhongHoc(rs.getString("phong_hoc"));
        schedule.setGhiChu(rs.getString("ghi_chu"));
        schedule.setCreatedAt(rs.getTimestamp("created_at"));
        return schedule;
    }
}

