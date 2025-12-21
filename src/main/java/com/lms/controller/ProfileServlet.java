package com.lms.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lms.dao.UserDAO;
import com.lms.dao.UserProfileDAO;
import com.lms.model.User;
import com.lms.model.UserProfile;

/**
 * Servlet xử lý hồ sơ cá nhân
 * GET: Hiển thị form cập nhật hồ sơ
 * POST: Xử lý cập nhật hồ sơ
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Hiển thị form cập nhật hồ sơ
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Đảm bảo encoding UTF-8
        response.setCharacterEncoding("UTF-8");
        
        // Lấy user từ session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Lấy profile của user
        UserProfileDAO profileDAO = new UserProfileDAO();
        UserProfile profile = profileDAO.findByUserId(user.getId());
        
        // Nếu chưa có profile, tạo profile rỗng
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(user.getId());
        }
        
        // Lấy message từ session (nếu có) và xóa sau khi lấy
        String successMessage = null;
        String errorMessage = null;
        if (session != null) {
            successMessage = (String) session.getAttribute("successMessage");
            errorMessage = (String) session.getAttribute("errorMessage");
            if (successMessage != null) {
                session.removeAttribute("successMessage");
            }
            if (errorMessage != null) {
                session.removeAttribute("errorMessage");
            }
        }
        
        // Set vào request attribute
        request.setAttribute("profile", profile);
        request.setAttribute("user", user);
        request.setAttribute("successMessage", successMessage);
        request.setAttribute("errorMessage", errorMessage);
        
        // Forward đến profile.jsp
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
    
    /**
     * Xử lý cập nhật hồ sơ
     * Sử dụng forward với message trong session để tránh redirect và alert
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Đảm bảo encoding UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Lấy user từ session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Lấy dữ liệu từ form (đã được filter xử lý encoding)
        String hoTen = request.getParameter("hoTen");
        String chuyenNganh = request.getParameter("chuyenNganh");
        String namHoc = request.getParameter("namHoc");
        
        // Validate: không cần bắt buộc, nhưng nếu có thì không rỗng
        if (hoTen != null && hoTen.trim().isEmpty()) {
            hoTen = null;
        }
        if (chuyenNganh != null && chuyenNganh.trim().isEmpty()) {
            chuyenNganh = null;
        }
        if (namHoc != null && namHoc.trim().isEmpty()) {
            namHoc = null;
        }
        
        // Tạo/Update UserProfile
        UserProfile profile = new UserProfile(user.getId(), hoTen, chuyenNganh, namHoc);
        
        UserProfileDAO profileDAO = new UserProfileDAO();
        boolean success = profileDAO.createOrUpdate(profile);
        
        // Đồng bộ hoTen từ UserProfile sang User nếu có thay đổi
        if (success && hoTen != null && !hoTen.trim().isEmpty()) {
            UserDAO userDAO = new UserDAO();
            userDAO.updateHoTen(user.getId(), hoTen.trim());
            // Cập nhật user trong session
            user.setHoTen(hoTen.trim());
            session.setAttribute("user", user);
        }
        
        // Lấy lại profile sau khi cập nhật để hiển thị
        profile = profileDAO.findByUserId(user.getId());
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(user.getId());
        }
        
        // Set message vào session thay vì redirect
        if (session != null) {
            if (success) {
                session.setAttribute("successMessage", "Cập nhật hồ sơ thành công!");
                session.removeAttribute("errorMessage");
            } else {
                session.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật hồ sơ. Vui lòng thử lại.");
                session.removeAttribute("successMessage");
            }
        }
        
        // Set dữ liệu vào request và forward (không redirect)
        request.setAttribute("profile", profile);
        request.setAttribute("user", user);
        
        // Forward đến profile.jsp
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
}

