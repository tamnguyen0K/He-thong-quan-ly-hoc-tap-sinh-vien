package com.lms.service;

import com.lms.dao.GradeDAO;
import com.lms.model.Grade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Service tính toán GPA
 */
public class GPAService {
    
    /**
     * Tính GPA học kỳ
     */
    public double calculateTermGPA(int userId, String hocKy) {
        GradeDAO gradeDAO = new GradeDAO();
        List<Grade> grades = gradeDAO.findByUserIdAndTerm(userId, hocKy);
        
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
     * Tính GPA tổng
     */
    public double calculateOverallGPA(int userId) {
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
}

