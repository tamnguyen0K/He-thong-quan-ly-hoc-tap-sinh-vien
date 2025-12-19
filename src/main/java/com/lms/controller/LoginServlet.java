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
import javax.servlet.http.HttpSession;

/**
 * Servlet xử lý đăng nhập
 * GET: Hiển thị form đăng nhập
 * POST: Xử lý đăng nhập
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Hiển thị form đăng nhập
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy thông báo thành công từ parameter (nếu có)
        String success = request.getParameter("success");
        if (success != null) {
            request.setAttribute("success", success);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    
    /**
     * Xử lý đăng nhập
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validate
        String error = null;
        
        if (email == null || email.trim().isEmpty()) {
            error = "Email không được để trống";
        } else if (password == null || password.trim().isEmpty()) {
            error = "Mật khẩu không được để trống";
        }
        
        // Nếu có lỗi validate, forward lại form
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        
        // Tìm user theo email
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByEmail(email);
        
        if (user == null) {
            // User không tồn tại
            error = "Email hoặc mật khẩu không đúng";
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra password
        boolean passwordValid = PasswordUtil.checkPassword(password, user.getPasswordHash());
        
        if (!passwordValid) {
            // Password không đúng
            error = "Email hoặc mật khẩu không đúng";
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        
        // Đăng nhập thành công - tạo session
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        
        // Lấy redirect URL (nếu có)
        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.isEmpty()) {
            response.sendRedirect(redirect);
        } else {
            // Redirect đến dashboard
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
}

