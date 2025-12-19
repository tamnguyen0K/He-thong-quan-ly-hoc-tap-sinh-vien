package com.lms.controller;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import com.lms.util.PasswordUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet xử lý đăng ký tài khoản
 * GET: Hiển thị form đăng ký
 * POST: Xử lý đăng ký
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Hiển thị form đăng ký
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
    
    /**
     * Xử lý đăng ký
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate
        String error = null;
        
        if (email == null || email.trim().isEmpty()) {
            error = "Email không được để trống";
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            error = "Email không hợp lệ";
        } else if (password == null || password.trim().isEmpty()) {
            error = "Mật khẩu không được để trống";
        } else if (password.length() < 6) {
            error = "Mật khẩu phải có ít nhất 6 ký tự";
        } else if (!password.equals(confirmPassword)) {
            error = "Mật khẩu và xác nhận mật khẩu không khớp";
        }
        
        // Nếu có lỗi, forward lại form với thông báo lỗi
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra email đã tồn tại chưa
        UserDAO userDAO = new UserDAO();
        User existingUser = userDAO.findByEmail(email);
        
        if (existingUser != null) {
            error = "Email này đã được sử dụng";
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        // Hash password
        String passwordHash = PasswordUtil.hashPassword(password);
        
        // Tạo user mới
        User newUser = new User(email, passwordHash, "student");
        int userId = userDAO.create(newUser);
        
        if (userId > 0) {
            // Đăng ký thành công, redirect đến trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/login?success=Đăng ký thành công! Vui lòng đăng nhập.");
        } else {
            // Lỗi khi tạo user
            error = "Có lỗi xảy ra khi đăng ký. Vui lòng thử lại.";
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}

