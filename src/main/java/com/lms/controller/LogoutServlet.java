package com.lms.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet xử lý đăng xuất
 * Xóa session và redirect về trang đăng nhập
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy session (không tạo mới nếu chưa có)
        HttpSession session = request.getSession(false);
        
        // Nếu có session, xóa nó
        if (session != null) {
            session.invalidate();
        }
        
        // Redirect về trang đăng nhập
        response.sendRedirect(request.getContextPath() + "/login");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

