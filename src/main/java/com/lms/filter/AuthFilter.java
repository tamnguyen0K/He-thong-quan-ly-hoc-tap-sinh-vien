package com.lms.filter;

import com.lms.model.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filter để bảo vệ các trang cần đăng nhập
 * Cho phép truy cập /login, /register, /logout và các file tĩnh
 * Các trang khác yêu cầu đăng nhập
 */
@WebFilter("/*")
public class AuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần khởi tạo gì
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // Loại bỏ context path để lấy path tương đối
        String relativePath = path.substring(contextPath.length());
        
        // Cho phép truy cập không cần đăng nhập
        if (isPublicPath(relativePath)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Kiểm tra session
        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        if (user == null) {
            // Chưa đăng nhập, redirect đến trang đăng nhập
            // Lưu URL hiện tại để redirect lại sau khi đăng nhập
            String redirectUrl = httpRequest.getRequestURI();
            if (httpRequest.getQueryString() != null) {
                redirectUrl += "?" + httpRequest.getQueryString();
            }
            httpResponse.sendRedirect(contextPath + "/login?redirect=" + 
                    java.net.URLEncoder.encode(redirectUrl, "UTF-8"));
            return;
        }
        
        // Đã đăng nhập, cho phép tiếp tục
        chain.doFilter(request, response);
    }
    
    /**
     * Kiểm tra path có phải public path không (không cần đăng nhập)
     * @param path Path cần kiểm tra
     * @return true nếu là public path
     */
    private boolean isPublicPath(String path) {
        // Cho phép login, register, logout
        if (path.equals("/login") || path.equals("/register") || path.equals("/logout")) {
            return true;
        }
        
        // Cho phép các file tĩnh
        if (path.startsWith("/assets/") || 
            path.startsWith("/css/") || 
            path.startsWith("/js/") || 
            path.startsWith("/images/") ||
            path.startsWith("/uploads/")) {
            return true;
        }
        
        // Cho phép trang home (có thể xem khi chưa đăng nhập)
        if (path.equals("/") || path.equals("/home")) {
            return true;
        }
        
        // Cho phép test-dao (có thể xóa sau)
        return path.equals("/test-dao");
    }
    
    @Override
    public void destroy() {
        // Không cần cleanup
    }
}

