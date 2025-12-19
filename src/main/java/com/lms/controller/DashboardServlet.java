package com.lms.controller;

import com.lms.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet xử lý trang dashboard
 * Hiển thị trang chủ sau khi đăng nhập
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra session có user không (tạm thời, sẽ có Filter sau)
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        if (user == null) {
            // Chưa đăng nhập, redirect đến trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Set user vào request attribute để hiển thị trong JSP
        request.setAttribute("user", user);
        
        // Forward đến dashboard.jsp
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}

