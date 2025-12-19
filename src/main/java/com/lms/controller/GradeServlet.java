package com.lms.controller;

import com.lms.dao.GradeDAO;
import com.lms.model.Grade;
import com.lms.model.User;
import com.lms.service.GPAService;
import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        
        if (term != null && !term.isEmpty()) {
            request.setAttribute("grades", gradeDAO.findByUserIdAndTerm(user.getId(), term));
            request.setAttribute("termGPA", gpaService.calculateTermGPA(user.getId(), term));
        } else {
            request.setAttribute("grades", gradeDAO.findAllByUserId(user.getId()));
        }
        
        request.setAttribute("overallGPA", gpaService.calculateOverallGPA(user.getId()));
        request.setAttribute("terms", gradeDAO.findAllTermsByUserId(user.getId()));
        request.setAttribute("selectedTerm", term);
        
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
                    gpaService.calculateTermGPA(user.getId(), g.getHocKy());
                    gpaService.calculateOverallGPA(user.getId());
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
                    String oldHocKy = g.getHocKy();
                    g.setTenMon(request.getParameter("tenMon"));
                    g.setHocKy(request.getParameter("hocKy"));
                    g.setTinChi(Integer.parseInt(request.getParameter("tinChi")));
                    g.setDiemSo(new BigDecimal(request.getParameter("diemSo")));
                    g.setLoaiDiem(request.getParameter("loaiDiem"));
                    g.setGhiChu(request.getParameter("ghiChu"));
                    if (gradeDAO.update(g)) {
                        gpaService.calculateTermGPA(user.getId(), g.getHocKy());
                        if (!g.getHocKy().equals(oldHocKy)) {
                            gpaService.calculateTermGPA(user.getId(), oldHocKy);
                        }
                        gpaService.calculateOverallGPA(user.getId());
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
                    String hocKy = g.getHocKy();
                    if (gradeDAO.delete(id)) {
                        gpaService.calculateTermGPA(user.getId(), hocKy);
                        gpaService.calculateOverallGPA(user.getId());
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

