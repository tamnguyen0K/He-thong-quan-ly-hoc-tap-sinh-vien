package com.lms.controller;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet test để kiểm tra UserDAO
 * Chỉ dùng cho mục đích test, có thể xóa sau
 */
@WebServlet("/test-dao")
public class TestDAOServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        UserDAO userDAO = new UserDAO();
        
        out.println("<html><head><title>Test DAO</title></head><body>");
        out.println("<h1>Test UserDAO</h1>");
        
        // Test 1: Tạo user mới
        out.println("<h2>Test 1: Tạo user mới</h2>");
        User testUser = new User("test@example.com", "hashed_password_123", "student");
        int userId = userDAO.create(testUser);
        if (userId > 0) {
            out.println("<p style='color: green;'>✓ Tạo user thành công! ID: " + userId + "</p>");
        } else {
            out.println("<p style='color: red;'>✗ Lỗi khi tạo user</p>");
        }
        
        // Test 2: Tìm user theo email
        out.println("<h2>Test 2: Tìm user theo email</h2>");
        User foundUser = userDAO.findByEmail("test@example.com");
        if (foundUser != null) {
            out.println("<p style='color: green;'>✓ Tìm thấy user: " + foundUser.getEmail() + "</p>");
        } else {
            out.println("<p style='color: red;'>✗ Không tìm thấy user</p>");
        }
        
        // Test 3: Validate login
        out.println("<h2>Test 3: Validate login</h2>");
        User loginUser = userDAO.validateLogin("test@example.com", "hashed_password_123");
        if (loginUser != null) {
            out.println("<p style='color: green;'>✓ Login hợp lệ</p>");
        } else {
            out.println("<p style='color: red;'>✗ Login không hợp lệ</p>");
        }
        
        out.println("</body></html>");
    }
}

