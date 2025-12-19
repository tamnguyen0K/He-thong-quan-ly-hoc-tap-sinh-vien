package com.lms.controller;

import com.lms.dao.ScheduleDAO;
import com.lms.model.Schedule;
import com.lms.model.User;
import java.io.IOException;
import java.sql.Time;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet xử lý CRUD lịch học
 * GET: Hiển thị danh sách lịch học
 * POST: Xử lý thêm/sửa/xóa lịch học
 */
@WebServlet("/schedule")
public class ScheduleServlet extends HttpServlet {
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
        
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        request.setAttribute("schedules", scheduleDAO.findAllByUserId(user.getId()));
        
        // Lấy schedule để edit (nếu có)
        String editId = request.getParameter("edit");
        if (editId != null) {
            try {
                Schedule schedule = scheduleDAO.findById(Integer.parseInt(editId));
                if (schedule != null && schedule.getUserId() == user.getId()) {
                    request.setAttribute("editSchedule", schedule);
                }
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        
        request.getRequestDispatcher("/WEB-INF/views/schedule.jsp").forward(request, response);
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
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        String message = null;
        
        switch (action) {
        case "add":
            message = handleAdd(request, user.getId(), scheduleDAO);
            break;
        case "update":
            message = handleUpdate(request, user.getId(), scheduleDAO);
            break;
        case "delete":
            message = handleDelete(request, user.getId(), scheduleDAO);
            break;
        default:
            break;
        }
        
        response.sendRedirect(request.getContextPath() + "/schedule" + 
            (message != null ? "?message=" + java.net.URLEncoder.encode(message, "UTF-8") : ""));
    }
    
    private String handleAdd(HttpServletRequest request, int userId, ScheduleDAO scheduleDAO) {
        String tenMon = request.getParameter("tenMon");
        String thuTrongTuan = request.getParameter("thuTrongTuan");
        String thoiGianBatDau = request.getParameter("thoiGianBatDau");
        String thoiGianKetThuc = request.getParameter("thoiGianKetThuc");
        String phongHoc = request.getParameter("phongHoc");
        String ghiChu = request.getParameter("ghiChu");
        
        if (tenMon == null || tenMon.trim().isEmpty() ||
            thuTrongTuan == null || thoiGianBatDau == null || thoiGianKetThuc == null) {
            return "Vui lòng điền đầy đủ thông tin bắt buộc";
        }
        
        Time batDau = Time.valueOf(thoiGianBatDau + ":00");
        Time ketThuc = Time.valueOf(thoiGianKetThuc + ":00");
        
        if (ketThuc.before(batDau) || ketThuc.equals(batDau)) {
            return "Thời gian kết thúc phải sau thời gian bắt đầu";
        }
        
        if (scheduleDAO.checkConflict(userId, thuTrongTuan, batDau, ketThuc, -1)) {
            return "Lịch học bị trùng với lịch đã có";
        }
        
        Schedule schedule = new Schedule();
        schedule.setUserId(userId);
        schedule.setTenMon(tenMon.trim());
        schedule.setThuTrongTuan(thuTrongTuan);
        schedule.setThoiGianBatDau(batDau);
        schedule.setThoiGianKetThuc(ketThuc);
        schedule.setPhongHoc(phongHoc != null ? phongHoc.trim() : null);
        schedule.setGhiChu(ghiChu != null ? ghiChu.trim() : null);
        
        if (scheduleDAO.create(schedule) > 0) {
            return "Thêm lịch học thành công";
        }
        return "Có lỗi xảy ra khi thêm lịch học";
    }
    
    private String handleUpdate(HttpServletRequest request, int userId, ScheduleDAO scheduleDAO) {
        String idStr = request.getParameter("id");
        if (idStr == null) return "Thiếu ID";
        
        try {
            int id = Integer.parseInt(idStr);
            Schedule existing = scheduleDAO.findById(id);
            if (existing == null || existing.getUserId() != userId) {
                return "Không tìm thấy lịch học";
            }
            
            String tenMon = request.getParameter("tenMon");
            String thuTrongTuan = request.getParameter("thuTrongTuan");
            String thoiGianBatDau = request.getParameter("thoiGianBatDau");
            String thoiGianKetThuc = request.getParameter("thoiGianKetThuc");
            String phongHoc = request.getParameter("phongHoc");
            String ghiChu = request.getParameter("ghiChu");
            
            if (tenMon == null || tenMon.trim().isEmpty() ||
                thuTrongTuan == null || thoiGianBatDau == null || thoiGianKetThuc == null) {
                return "Vui lòng điền đầy đủ thông tin bắt buộc";
            }
            
            Time batDau = Time.valueOf(thoiGianBatDau + ":00");
            Time ketThuc = Time.valueOf(thoiGianKetThuc + ":00");
            
            if (ketThuc.before(batDau) || ketThuc.equals(batDau)) {
                return "Thời gian kết thúc phải sau thời gian bắt đầu";
            }
            
            if (scheduleDAO.checkConflict(userId, thuTrongTuan, batDau, ketThuc, id)) {
                return "Lịch học bị trùng với lịch đã có";
            }
            
            existing.setTenMon(tenMon.trim());
            existing.setThuTrongTuan(thuTrongTuan);
            existing.setThoiGianBatDau(batDau);
            existing.setThoiGianKetThuc(ketThuc);
            existing.setPhongHoc(phongHoc != null ? phongHoc.trim() : null);
            existing.setGhiChu(ghiChu != null ? ghiChu.trim() : null);
            
            if (scheduleDAO.update(existing)) {
                return "Cập nhật lịch học thành công";
            }
            return "Có lỗi xảy ra khi cập nhật lịch học";
        } catch (NumberFormatException e) {
            return "ID không hợp lệ";
        }
    }
    
    private String handleDelete(HttpServletRequest request, int userId, ScheduleDAO scheduleDAO) {
        String idStr = request.getParameter("id");
        if (idStr == null) return "Thiếu ID";
        
        try {
            int id = Integer.parseInt(idStr);
            Schedule existing = scheduleDAO.findById(id);
            if (existing == null || existing.getUserId() != userId) {
                return "Không tìm thấy lịch học";
            }
            
            if (scheduleDAO.delete(id)) {
                return "Xóa lịch học thành công";
            }
            return "Có lỗi xảy ra khi xóa lịch học";
        } catch (NumberFormatException e) {
            return "ID không hợp lệ";
        }
    }
}

