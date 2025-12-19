package com.lms.dao;

import com.lms.model.Grade;
import com.lms.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GradeDAO {
    
    public List<Grade> findAllByUserId(int userId) {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE user_id = ? ORDER BY hoc_ky, ten_mon";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách grades: " + e.getMessage());
        }
        return list;
    }
    
    public List<Grade> findByUserIdAndTerm(int userId, String hocKy) {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE user_id = ? AND hoc_ky = ? ORDER BY ten_mon";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, hocKy);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy grades theo học kỳ: " + e.getMessage());
        }
        return list;
    }
    
    public Grade findById(int id) {
        String sql = "SELECT * FROM grades WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm grade: " + e.getMessage());
        }
        return null;
    }
    
    public int create(Grade grade) {
        String sql = "INSERT INTO grades (user_id, ten_mon, hoc_ky, tin_chi, diem_so, loai_diem, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, grade.getUserId());
            pstmt.setString(2, grade.getTenMon());
            pstmt.setString(3, grade.getHocKy());
            pstmt.setInt(4, grade.getTinChi());
            pstmt.setBigDecimal(5, grade.getDiemSo());
            pstmt.setString(6, grade.getLoaiDiem());
            pstmt.setString(7, grade.getGhiChu());
            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo grade: " + e.getMessage());
        }
        return -1;
    }
    
    public boolean update(Grade grade) {
        String sql = "UPDATE grades SET ten_mon=?, hoc_ky=?, tin_chi=?, diem_so=?, loai_diem=?, ghi_chu=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, grade.getTenMon());
            pstmt.setString(2, grade.getHocKy());
            pstmt.setInt(3, grade.getTinChi());
            pstmt.setBigDecimal(4, grade.getDiemSo());
            pstmt.setString(5, grade.getLoaiDiem());
            pstmt.setString(6, grade.getGhiChu());
            pstmt.setInt(7, grade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật grade: " + e.getMessage());
        }
        return false;
    }
    
    public boolean delete(int id) {
        String sql = "DELETE FROM grades WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa grade: " + e.getMessage());
        }
        return false;
    }
    
    public Set<String> findAllTermsByUserId(int userId) {
        Set<String> terms = new HashSet<>();
        String sql = "SELECT DISTINCT hoc_ky FROM grades WHERE user_id = ? ORDER BY hoc_ky";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    terms.add(rs.getString("hoc_ky"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách học kỳ: " + e.getMessage());
        }
        return terms;
    }
    
    private Grade mapResultSet(ResultSet rs) throws SQLException {
        Grade g = new Grade();
        g.setId(rs.getInt("id"));
        g.setUserId(rs.getInt("user_id"));
        g.setTenMon(rs.getString("ten_mon"));
        g.setHocKy(rs.getString("hoc_ky"));
        g.setTinChi(rs.getInt("tin_chi"));
        g.setDiemSo(rs.getBigDecimal("diem_so"));
        g.setLoaiDiem(rs.getString("loai_diem"));
        g.setGhiChu(rs.getString("ghi_chu"));
        g.setCreatedAt(rs.getTimestamp("created_at"));
        g.setUpdatedAt(rs.getTimestamp("updated_at"));
        return g;
    }
}

