package com.lms.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.lms.dao.DocumentDAO;
import com.lms.model.Document;
import com.lms.model.User;
import com.lms.util.FileUtil;

@WebServlet("/documents/upload")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024) // 10MB
public class UploadDocumentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Part filePart = request.getPart("file");
            if (filePart == null || filePart.getSize() == 0) {
                response.sendRedirect(request.getContextPath() + "/documents?error=Không có file");
                return;
            }

            String fileName = filePart.getSubmittedFileName();
            if (!FileUtil.isAllowedFileType(fileName)) {
                response.sendRedirect(request.getContextPath() + "/documents?error=File không hợp lệ (chỉ DOC/DOCX/TXT)");
                return;
            }

            if (filePart.getSize() > 10 * 1024 * 1024) {
                response.sendRedirect(request.getContextPath() + "/documents?error=File quá lớn (tối đa 10MB)");
                return;
            }

            String uniqueFileName = FileUtil.generateUniqueFileName(fileName);
            String uploadDir = FileUtil.getUploadDirectory(getServletContext());
            String filePath = uploadDir + java.io.File.separator + uniqueFileName;

            if (FileUtil.saveFile(filePart.getInputStream(), uniqueFileName, uploadDir)) {
                Document doc = new Document();
                doc.setUserId(user.getId());
                doc.setTenFileGoc(fileName);
                doc.setTenFileLuu(uniqueFileName);
                doc.setDuongDan(filePath);
                doc.setLoaiFile(FileUtil.getFileExtension(fileName));
                doc.setKichThuoc(filePart.getSize());
                doc.setMonHoc(request.getParameter("monHoc"));
                doc.setMoTa(request.getParameter("moTa"));

                DocumentDAO dao = new DocumentDAO();
                if (dao.create(doc) > 0) {
                    response.sendRedirect(request.getContextPath() + "/documents?message=Upload thành công");
                } else {
                    new java.io.File(filePath).delete();
                    response.sendRedirect(request.getContextPath() + "/documents?error=Lỗi khi lưu");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/documents?error=Lỗi khi upload");
            }
        } catch (IOException | ServletException e) {
            response.sendRedirect(request.getContextPath() + "/documents?error=" + e.getMessage());
        }
    }
}
