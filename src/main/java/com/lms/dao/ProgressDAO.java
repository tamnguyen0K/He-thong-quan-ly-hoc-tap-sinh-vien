package com.lms.dao;

import com.lms.model.Progress;
import com.lms.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho bảng progress
 */
public class ProgressDAO {
    
    public List<Progress> findAllByUserId(int userId) {
        List<Progress> list = new ArrayList<>();
        String sql = "SELECT * FROM progress WHERE user_id = ? ORDER BY han_nop ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách progress: " + e.getMessage());
        }
        return list;
    }
    
    public Progress findById(int id) {
        String sql = "SELECT * FROM progress WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm progress: " + e.getMessage());
        }
        return null;
    }
    
    public int create(Progress progress) {
        String sql = "INSERT INTO progress (user_id, ten_task, loai_task, mon_hoc, trang_thai, han_nop, mo_ta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, progress.getUserId());
            pstmt.setString(2, progress.getTenTask());
            pstmt.setString(3, progress.getLoaiTask());
            pstmt.setString(4, progress.getMonHoc());
            pstmt.setString(5, progress.getTrangThai() != null ? progress.getTrangThai() : "chuaxong");
            pstmt.setTimestamp(6, progress.getHanNop());
            pstmt.setString(7, progress.getMoTa());
            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo progress: " + e.getMessage());
        }
        return -1;
    }
    
    public boolean update(Progress progress) {
        String sql = "UPDATE progress SET ten_task=?, loai_task=?, mon_hoc=?, han_nop=?, mo_ta=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, progress.getTenTask());
            pstmt.setString(2, progress.getLoaiTask());
            pstmt.setString(3, progress.getMonHoc());
            pstmt.setTimestamp(4, progress.getHanNop());
            pstmt.setString(5, progress.getMoTa());
            pstmt.setInt(6, progress.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật progress: " + e.getMessage());
        }
        return false;
    }
    
    public boolean toggleStatus(int id) {
        String sql = "UPDATE progress SET trang_thai = CASE WHEN trang_thai='chuaxong' THEN 'daxong' ELSE 'chuaxong' END, " +
                     "completed_at = CASE WHEN trang_thai='chuaxong' THEN NOW() ELSE NULL END WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi toggle status: " + e.getMessage());
        }
        return false;
    }
    
    public boolean delete(int id) {
        String sql = "DELETE FROM progress WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa progress: " + e.getMessage());
        }
        return false;
    }
    
    private Progress mapResultSet(ResultSet rs) throws SQLException {
        Progress p = new Progress();
        p.setId(rs.getInt("id"));
        p.setUserId(rs.getInt("user_id"));
        p.setTenTask(rs.getString("ten_task"));
        p.setLoaiTask(rs.getString("loai_task"));
        p.setMonHoc(rs.getString("mon_hoc"));
        p.setTrangThai(rs.getString("trang_thai"));
        p.setHanNop(rs.getTimestamp("han_nop"));
        p.setMoTa(rs.getString("mo_ta"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setCompletedAt(rs.getTimestamp("completed_at"));
        return p;
    }
}

