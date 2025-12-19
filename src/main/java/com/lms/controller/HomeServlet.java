package com.lms.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lms.model.User;

/**
 * Servlet xử lý trang chủ
 * Forward request đến trang home.jsp
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/", "/home"})
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Xử lý GET request
     * Forward đến trang home.jsp
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy session để kiểm tra user đã đăng nhập chưa
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                request.setAttribute("user", user);
            }
        }
        
        // Forward đến trang home.jsp
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }

    /**
     * Xử lý POST request (nếu cần)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

