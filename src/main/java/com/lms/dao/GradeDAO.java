package com.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lms.model.Grade;
import com.lms.util.DBUtil;

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
        String sql = "INSERT INTO grades (user_id, ten_mon, nam_hoc, hoc_ky, tin_chi, diem_so, loai_diem, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, grade.getUserId());
            pstmt.setString(2, grade.getTenMon());
            pstmt.setString(3, grade.getNamHoc());
            pstmt.setString(4, grade.getHocKy());
            pstmt.setInt(5, grade.getTinChi());
            pstmt.setBigDecimal(6, grade.getDiemSo());
            pstmt.setString(7, grade.getLoaiDiem());
            pstmt.setString(8, grade.getGhiChu());
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
        String sql = "UPDATE grades SET ten_mon=?, nam_hoc=?, hoc_ky=?, tin_chi=?, diem_so=?, loai_diem=?, ghi_chu=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, grade.getTenMon());
            pstmt.setString(2, grade.getNamHoc());
            pstmt.setString(3, grade.getHocKy());
            pstmt.setInt(4, grade.getTinChi());
            pstmt.setBigDecimal(5, grade.getDiemSo());
            pstmt.setString(6, grade.getLoaiDiem());
            pstmt.setString(7, grade.getGhiChu());
            pstmt.setInt(8, grade.getId());
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
        g.setNamHoc(rs.getString("nam_hoc"));
        g.setHocKy(rs.getString("hoc_ky"));
        g.setTinChi(rs.getInt("tin_chi"));
        g.setDiemSo(rs.getBigDecimal("diem_so"));
        g.setLoaiDiem(rs.getString("loai_diem"));
        g.setGhiChu(rs.getString("ghi_chu"));
        g.setCreatedAt(rs.getTimestamp("created_at"));
        g.setUpdatedAt(rs.getTimestamp("updated_at"));
        return g;
    }
    
    /**
     * Lấy danh sách các môn học đã có của user (distinct với nam_hoc, hoc_ky, tin_chi)
     * Để dùng cho dropdown chọn môn
     */
    public List<Grade> findDistinctSubjectsByUserId(int userId) {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT DISTINCT ten_mon, nam_hoc, hoc_ky, tin_chi FROM grades WHERE user_id = ? ORDER BY ten_mon";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Grade g = new Grade();
                    g.setTenMon(rs.getString("ten_mon"));
                    g.setNamHoc(rs.getString("nam_hoc"));
                    g.setHocKy(rs.getString("hoc_ky"));
                    g.setTinChi(rs.getInt("tin_chi"));
                    list.add(g);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách môn học: " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Kiểm tra xem môn học đã có đủ 3 loại điểm chưa (quatrinh, giuaky, ketthuc)
     */
    public boolean hasAllThreeGradeTypes(int userId, String tenMon, String namHoc, String hocKy) {
        String sql = "SELECT COUNT(DISTINCT loai_diem) as count FROM grades WHERE user_id = ? AND ten_mon = ? AND (nam_hoc = ? OR (nam_hoc IS NULL AND ? IS NULL)) AND hoc_ky = ? AND loai_diem IN ('quatrinh', 'giuaky', 'ketthuc')";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, tenMon);
            pstmt.setString(3, namHoc);
            pstmt.setString(4, namHoc);
            pstmt.setString(5, hocKy);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") >= 3;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra loại điểm: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Lấy điểm của một môn học theo loại điểm
     */
    public Grade findByUserAndSubjectAndType(int userId, String tenMon, String namHoc, String hocKy, String loaiDiem) {
        String sql = "SELECT * FROM grades WHERE user_id = ? AND ten_mon = ? AND (nam_hoc = ? OR (nam_hoc IS NULL AND ? IS NULL)) AND hoc_ky = ? AND loai_diem = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, tenMon);
            pstmt.setString(3, namHoc);
            pstmt.setString(4, namHoc);
            pstmt.setString(5, hocKy);
            pstmt.setString(6, loaiDiem);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm grade: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Lấy tất cả điểm của một môn học (theo ten_mon, nam_hoc, hoc_ky)
     */
    public List<Grade> findByUserAndSubject(int userId, String tenMon, String namHoc, String hocKy) {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE user_id = ? AND ten_mon = ? AND (nam_hoc = ? OR (nam_hoc IS NULL AND ? IS NULL)) AND hoc_ky = ? ORDER BY FIELD(loai_diem, 'quatrinh', 'giuaky', 'ketthuc')";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, tenMon);
            pstmt.setString(3, namHoc);
            pstmt.setString(4, namHoc);
            pstmt.setString(5, hocKy);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy điểm theo môn: " + e.getMessage());
        }
        return list;
    }
}

