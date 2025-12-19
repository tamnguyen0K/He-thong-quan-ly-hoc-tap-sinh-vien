package com.lms.dao;

import com.lms.model.Document;
import com.lms.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {
    
    public List<Document> findAllByUserId(int userId) {
        List<Document> list = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE user_id = ? ORDER BY uploaded_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách documents: " + e.getMessage());
        }
        return list;
    }
    
    public List<Document> findByUserIdAndMonHoc(int userId, String monHoc) {
        List<Document> list = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE user_id = ? AND mon_hoc = ? ORDER BY uploaded_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, monHoc);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy documents theo môn học: " + e.getMessage());
        }
        return list;
    }
    
    public Document findById(int id) {
        String sql = "SELECT * FROM documents WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm document: " + e.getMessage());
        }
        return null;
    }
    
    public int create(Document doc) {
        String sql = "INSERT INTO documents (user_id, ten_file_goc, ten_file_luu, duong_dan, loai_file, kich_thuoc, mon_hoc, mo_ta) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, doc.getUserId());
            pstmt.setString(2, doc.getTenFileGoc());
            pstmt.setString(3, doc.getTenFileLuu());
            pstmt.setString(4, doc.getDuongDan());
            pstmt.setString(5, doc.getLoaiFile());
            pstmt.setLong(6, doc.getKichThuoc());
            pstmt.setString(7, doc.getMonHoc());
            pstmt.setString(8, doc.getMoTa());
            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo document: " + e.getMessage());
        }
        return -1;
    }
    
    public boolean delete(int id) {
        String sql = "DELETE FROM documents WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa document: " + e.getMessage());
        }
        return false;
    }
    
    private Document mapResultSet(ResultSet rs) throws SQLException {
        Document d = new Document();
        d.setId(rs.getInt("id"));
        d.setUserId(rs.getInt("user_id"));
        d.setTenFileGoc(rs.getString("ten_file_goc"));
        d.setTenFileLuu(rs.getString("ten_file_luu"));
        d.setDuongDan(rs.getString("duong_dan"));
        d.setLoaiFile(rs.getString("loai_file"));
        d.setKichThuoc(rs.getLong("kich_thuoc"));
        d.setMonHoc(rs.getString("mon_hoc"));
        d.setMoTa(rs.getString("mo_ta"));
        d.setUploadedAt(rs.getTimestamp("uploaded_at"));
        return d;
    }
}

