package com.lms.controller;

import com.lms.dao.ProgressDAO;
import com.lms.model.Progress;
import com.lms.model.User;
import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/progress")
public class ProgressServlet extends HttpServlet {
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
        
        ProgressDAO dao = new ProgressDAO();
        request.setAttribute("tasks", dao.findAllByUserId(user.getId()));
        
        String editId = request.getParameter("edit");
        if (editId != null) {
            try {
                Progress task = dao.findById(Integer.parseInt(editId));
                if (task != null && task.getUserId() == user.getId()) {
                    request.setAttribute("editTask", task);
                }
            } catch (NumberFormatException e) {}
        }
        
        request.getRequestDispatcher("/WEB-INF/views/progress.jsp").forward(request, response);
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
        ProgressDAO dao = new ProgressDAO();
        String message = null;
        
        switch (action) {
        case "add": {
            Progress p = new Progress();
            p.setUserId(user.getId());
            p.setTenTask(request.getParameter("tenTask"));
            p.setLoaiTask(request.getParameter("loaiTask"));
            p.setMonHoc(request.getParameter("monHoc"));
            p.setMoTa(request.getParameter("moTa"));
            try {
                String hanNop = request.getParameter("hanNop");
                if (hanNop != null && !hanNop.isEmpty()) {
                    p.setHanNop(Timestamp.valueOf(hanNop.replace("T", " ") + ":00"));
                }
            } catch (IllegalArgumentException e) {}
            if (dao.create(p) > 0) message = "Thêm task thành công";
            else message = "Lỗi khi thêm task";
            break;
        }
        case "update": {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Progress p = dao.findById(id);
                if (p != null && p.getUserId() == user.getId()) {
                    p.setTenTask(request.getParameter("tenTask"));
                    p.setLoaiTask(request.getParameter("loaiTask"));
                    p.setMonHoc(request.getParameter("monHoc"));
                    p.setMoTa(request.getParameter("moTa"));
                    try {
                        String hanNop = request.getParameter("hanNop");
                        if (hanNop != null && !hanNop.isEmpty()) {
                            p.setHanNop(Timestamp.valueOf(hanNop.replace("T", " ") + ":00"));
                        }
                    } catch (IllegalArgumentException e) {}
                    if (dao.update(p)) message = "Cập nhật thành công";
                    else message = "Lỗi khi cập nhật";
                }
            } catch (NumberFormatException e) {
                message = "ID không hợp lệ";
            }
            break;
        }
        case "toggle": {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                if (dao.toggleStatus(id)) message = "Cập nhật trạng thái thành công";
            } catch (NumberFormatException e) {}
            break;
        }
        case "delete": {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Progress p = dao.findById(id);
                if (p != null && p.getUserId() == user.getId() && dao.delete(id)) {
                    message = "Xóa thành công";
                }
            } catch (NumberFormatException e) {}
            break;
        }
        default:
            break;
        }
        
        response.sendRedirect(request.getContextPath() + "/progress" + 
            (message != null ? "?message=" + java.net.URLEncoder.encode(message, "UTF-8") : ""));
    }
}

