package com.lms.controller;

import com.lms.dao.DocumentDAO;
import com.lms.model.Document;
import com.lms.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/documents")
public class DocumentServlet extends HttpServlet {
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
        
        DocumentDAO dao = new DocumentDAO();
        String monHoc = request.getParameter("monHoc");
        
        if (monHoc != null && !monHoc.isEmpty()) {
            request.setAttribute("documents", dao.findByUserIdAndMonHoc(user.getId(), monHoc));
        } else {
            request.setAttribute("documents", dao.findAllByUserId(user.getId()));
        }
        
        request.setAttribute("selectedMonHoc", monHoc);
        request.getRequestDispatcher("/WEB-INF/views/documents.jsp").forward(request, response);
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
        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                DocumentDAO dao = new DocumentDAO();
                Document doc = dao.findById(id);
                if (doc != null && doc.getUserId() == user.getId()) {
                    // Xóa file
                    java.io.File file = new java.io.File(doc.getDuongDan());
                    file.delete();
                    // Xóa bản ghi
                    dao.delete(id);
                }
            } catch (NumberFormatException | SecurityException e) {}
        }
        
        response.sendRedirect(request.getContextPath() + "/documents");
    }
}

