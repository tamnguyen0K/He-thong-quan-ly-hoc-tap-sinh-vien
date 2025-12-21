package com.lms.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lms.dao.GradeDAO;
import com.lms.model.Grade;
import com.lms.model.User;
import com.lms.service.GPAService;

@WebServlet("/grades")
public class GradeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        GradeDAO gradeDAO = new GradeDAO();
        GPAService gpaService = new GPAService();
        String term = request.getParameter("term");
        
        // Lấy tất cả điểm (loại trừ điểm tổng kết) để hiển thị bảng điểm chi tiết
        List<Grade> allGrades = gradeDAO.findAllByUserId(user.getId());
        List<Grade> detailGrades = new java.util.ArrayList<>();
        for (Grade g : allGrades) {
            if (!"tongket".equals(g.getLoaiDiem())) {
                if (term == null || term.isEmpty() || term.equals(g.getHocKy())) {
                    detailGrades.add(g);
                }
            }
        }
        request.setAttribute("grades", detailGrades);
        
        // Lấy điểm tổng kết để hiển thị bảng tổng kết
        List<Grade> finalGrades = gpaService.getFinalGrades(user.getId());
        if (term != null && !term.isEmpty()) {
            List<Grade> filtered = new java.util.ArrayList<>();
            for (Grade g : finalGrades) {
                if (term.equals(g.getHocKy())) {
                    filtered.add(g);
                }
            }
            finalGrades = filtered;
        }
        request.setAttribute("finalGrades", finalGrades);
        
        // Tính điểm hệ 4 cho từng môn tổng kết
        java.util.Map<Integer, Double> grade4Map = new java.util.HashMap<>();
        for (Grade g : finalGrades) {
            grade4Map.put(g.getId(), gpaService.convertToGPA4(g.getDiemSo()));
        }
        request.setAttribute("grade4Map", grade4Map);
        
        request.setAttribute("overallGPA4", gpaService.calculateOverallGPA4(user.getId()));
        request.setAttribute("overallGPA10", gpaService.calculateOverallGPA10(user.getId()));
        request.setAttribute("terms", gradeDAO.findAllTermsByUserId(user.getId()));
        request.setAttribute("selectedTerm", term);
        
        // Lấy danh sách môn học đã có để hiển thị dropdown
        request.setAttribute("subjects", gradeDAO.findDistinctSubjectsByUserId(user.getId()));
        
        String editId = request.getParameter("edit");
        if (editId != null) {
            try {
                Grade grade = gradeDAO.findById(Integer.parseInt(editId));
                if (grade != null && grade.getUserId() == user.getId()) {
                    request.setAttribute("editGrade", grade);
                }
            } catch (NumberFormatException e) {}
        }
        
        request.getRequestDispatcher("/WEB-INF/views/grades.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        GradeDAO gradeDAO = new GradeDAO();
        GPAService gpaService = new GPAService();
        String message = null;
        
        switch (action) {
        case "add": {
            Grade g = new Grade();
            g.setUserId(user.getId());
            g.setTenMon(request.getParameter("tenMon"));
            g.setNamHoc(request.getParameter("namHoc"));
            g.setHocKy(request.getParameter("hocKy"));
            try {
                g.setTinChi(Integer.parseInt(request.getParameter("tinChi")));
                g.setDiemSo(new BigDecimal(request.getParameter("diemSo")));
            } catch (NumberFormatException e) {
                message = "Dữ liệu không hợp lệ";
            }
            g.setLoaiDiem(request.getParameter("loaiDiem"));
            g.setGhiChu(request.getParameter("ghiChu"));
            
            if (message == null) {
                if (gradeDAO.create(g) > 0) {
                    // Kiểm tra nếu đủ 3 loại điểm thì tự động tính và lưu điểm tổng kết
                    if (gradeDAO.hasAllThreeGradeTypes(user.getId(), g.getTenMon(), g.getNamHoc(), g.getHocKy())) {
                        gpaService.calculateAndSaveFinalGrade(user.getId(), g.getTenMon(), g.getNamHoc(), g.getHocKy());
                    }
                    message = "Thêm điểm thành công";
                } else {
                    message = "Lỗi khi thêm điểm";
                }
            }
            break;
        }
        case "update": {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Grade g = gradeDAO.findById(id);
                if (g != null && g.getUserId() == user.getId()) {
                    String oldTenMon = g.getTenMon();
                    String oldNamHoc = g.getNamHoc();
                    String oldHocKy = g.getHocKy();
                    g.setTenMon(request.getParameter("tenMon"));
                    g.setNamHoc(request.getParameter("namHoc"));
                    g.setHocKy(request.getParameter("hocKy"));
                    g.setTinChi(Integer.parseInt(request.getParameter("tinChi")));
                    g.setDiemSo(new BigDecimal(request.getParameter("diemSo")));
                    g.setLoaiDiem(request.getParameter("loaiDiem"));
                    g.setGhiChu(request.getParameter("ghiChu"));
                    if (gradeDAO.update(g)) {
                        // Kiểm tra và tính lại điểm tổng kết cho môn học cũ và mới
                        if (!g.getTenMon().equals(oldTenMon) || !g.getNamHoc().equals(oldNamHoc) || !g.getHocKy().equals(oldHocKy)) {
                            // Cập nhật lại điểm tổng kết cho môn học cũ (nếu có)
                            if (gradeDAO.hasAllThreeGradeTypes(user.getId(), oldTenMon, oldNamHoc, oldHocKy)) {
                                gpaService.calculateAndSaveFinalGrade(user.getId(), oldTenMon, oldNamHoc, oldHocKy);
                            }
                        }
                        // Kiểm tra và tính điểm tổng kết cho môn học mới
                        if (gradeDAO.hasAllThreeGradeTypes(user.getId(), g.getTenMon(), g.getNamHoc(), g.getHocKy())) {
                            gpaService.calculateAndSaveFinalGrade(user.getId(), g.getTenMon(), g.getNamHoc(), g.getHocKy());
                        }
                        message = "Cập nhật thành công";
                    }
                }
            } catch (NumberFormatException e) {
                message = "Lỗi khi cập nhật";
            }
            break;
        }
        case "delete": {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Grade g = gradeDAO.findById(id);
                if (g != null && g.getUserId() == user.getId()) {
                    String tenMon = g.getTenMon();
                    String namHoc = g.getNamHoc();
                    String hocKy = g.getHocKy();
                    if (gradeDAO.delete(id)) {
                        // Xóa điểm tổng kết nếu có
                        Grade finalGrade = gradeDAO.findByUserAndSubjectAndType(user.getId(), tenMon, namHoc, hocKy, "tongket");
                        if (finalGrade != null) {
                            gradeDAO.delete(finalGrade.getId());
                        }
                        // Kiểm tra lại và tính lại điểm tổng kết nếu vẫn còn đủ 3 loại
                        if (gradeDAO.hasAllThreeGradeTypes(user.getId(), tenMon, namHoc, hocKy)) {
                            gpaService.calculateAndSaveFinalGrade(user.getId(), tenMon, namHoc, hocKy);
                        }
                        message = "Xóa thành công";
                    }
                }
            } catch (NumberFormatException e) {
                message = "ID không hợp lệ";
            }
            break;
        }
        default:
            break;
        }
        
        response.sendRedirect(request.getContextPath() + "/grades" + 
            (message != null ? "?message=" + java.net.URLEncoder.encode(message, "UTF-8") : ""));
    }
}

