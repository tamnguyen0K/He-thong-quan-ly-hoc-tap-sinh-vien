package com.lms.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.lms.dao.GradeDAO;
import com.lms.model.Grade;

/**
 * Service tính toán GPA
 */
public class GPAService {
    
    /**
     * Tính điểm tổng kết từ 3 loại điểm: quá trình (20%), giữa kỳ (30%), kết thúc (50%)
     */
    public BigDecimal calculateFinalGrade(BigDecimal diemQuaTrinh, BigDecimal diemGiuaKy, BigDecimal diemKetThuc) {
        BigDecimal diemTongKet = BigDecimal.ZERO;
        
        if (diemQuaTrinh != null) {
            diemTongKet = diemTongKet.add(diemQuaTrinh.multiply(BigDecimal.valueOf(0.2)));
        }
        if (diemGiuaKy != null) {
            diemTongKet = diemTongKet.add(diemGiuaKy.multiply(BigDecimal.valueOf(0.3)));
        }
        if (diemKetThuc != null) {
            diemTongKet = diemTongKet.add(diemKetThuc.multiply(BigDecimal.valueOf(0.5)));
        }
        
        return diemTongKet.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Chuyển điểm hệ 10 sang điểm hệ 4
     */
    public double convertToGPA4(BigDecimal diem10) {
        if (diem10 == null) return 0.0;
        double diem = diem10.doubleValue();
        
        if (diem >= 9.0) return 4.0;
        else if (diem >= 8.0) return 3.5;
        else if (diem >= 7.0) return 3.0;
        else if (diem >= 6.5) return 2.5;
        else if (diem >= 5.5) return 2.0;
        else if (diem >= 5.0) return 1.5;
        else if (diem >= 4.0) return 1.0;
        else return 0.0;
    }
    
    /**
     * Tính và lưu điểm tổng kết khi đủ 3 loại điểm
     */
    public void calculateAndSaveFinalGrade(int userId, String tenMon, String namHoc, String hocKy) {
        GradeDAO gradeDAO = new GradeDAO();
        
        // Lấy 3 loại điểm
        Grade quaTrinh = gradeDAO.findByUserAndSubjectAndType(userId, tenMon, namHoc, hocKy, "quatrinh");
        Grade giuaKy = gradeDAO.findByUserAndSubjectAndType(userId, tenMon, namHoc, hocKy, "giuaky");
        Grade ketThuc = gradeDAO.findByUserAndSubjectAndType(userId, tenMon, namHoc, hocKy, "ketthuc");
        
        if (quaTrinh == null || giuaKy == null || ketThuc == null) {
            return; // Chưa đủ 3 loại điểm
        }
        
        // Tính điểm tổng kết
        BigDecimal diemTongKet = calculateFinalGrade(quaTrinh.getDiemSo(), giuaKy.getDiemSo(), ketThuc.getDiemSo());
        
        // Kiểm tra xem đã có điểm tổng kết chưa
        Grade existingFinal = gradeDAO.findByUserAndSubjectAndType(userId, tenMon, namHoc, hocKy, "tongket");
        
        if (existingFinal != null) {
            // Cập nhật điểm tổng kết
            existingFinal.setDiemSo(diemTongKet);
            gradeDAO.update(existingFinal);
        } else {
            // Tạo mới điểm tổng kết
            Grade finalGrade = new Grade();
            finalGrade.setUserId(userId);
            finalGrade.setTenMon(tenMon);
            finalGrade.setNamHoc(namHoc);
            finalGrade.setHocKy(hocKy);
            finalGrade.setTinChi(quaTrinh.getTinChi());
            finalGrade.setDiemSo(diemTongKet);
            finalGrade.setLoaiDiem("tongket");
            gradeDAO.create(finalGrade);
        }
    }
    
    /**
     * Tính GPA tổng hệ 4 (chỉ tính từ điểm tổng kết)
     */
    public double calculateOverallGPA4(int userId) {
        GradeDAO gradeDAO = new GradeDAO();
        List<Grade> grades = gradeDAO.findAllByUserId(userId);
        
        BigDecimal totalDiem4 = BigDecimal.ZERO;
        int totalTinChi = 0;
        
        for (Grade g : grades) {
            if ("tongket".equals(g.getLoaiDiem())) {
                double diem4 = convertToGPA4(g.getDiemSo());
                totalDiem4 = totalDiem4.add(BigDecimal.valueOf(diem4).multiply(BigDecimal.valueOf(g.getTinChi())));
                totalTinChi += g.getTinChi();
            }
        }
        
        if (totalTinChi == 0) return 0.0;
        return totalDiem4.divide(BigDecimal.valueOf(totalTinChi), 2, RoundingMode.HALF_UP).doubleValue();
    }
    
    /**
     * Tính GPA tổng hệ 10 (chỉ tính từ điểm tổng kết)
     */
    public double calculateOverallGPA10(int userId) {
        GradeDAO gradeDAO = new GradeDAO();
        List<Grade> grades = gradeDAO.findAllByUserId(userId);
        
        BigDecimal totalDiem = BigDecimal.ZERO;
        int totalTinChi = 0;
        
        for (Grade g : grades) {
            if ("tongket".equals(g.getLoaiDiem())) {
                totalDiem = totalDiem.add(g.getDiemSo().multiply(BigDecimal.valueOf(g.getTinChi())));
                totalTinChi += g.getTinChi();
            }
        }
        
        if (totalTinChi == 0) return 0.0;
        return totalDiem.divide(BigDecimal.valueOf(totalTinChi), 2, RoundingMode.HALF_UP).doubleValue();
    }
    
    /**
     * Lấy tất cả các môn học đã có điểm tổng kết để hiển thị bảng tổng kết
     */
    public List<Grade> getFinalGrades(int userId) {
        GradeDAO gradeDAO = new GradeDAO();
        List<Grade> allGrades = gradeDAO.findAllByUserId(userId);
        
        // Lọc chỉ lấy điểm tổng kết
        List<Grade> finalGrades = new java.util.ArrayList<>();
        for (Grade g : allGrades) {
            if ("tongket".equals(g.getLoaiDiem())) {
                finalGrades.add(g);
            }
        }
        
        return finalGrades;
    }
}

