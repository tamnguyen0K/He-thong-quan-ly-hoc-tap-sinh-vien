package com.lms.controller;

import com.lms.dao.ScheduleDAO;
import com.lms.model.Schedule;
import com.lms.model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet xử lý import lịch học từ file CSV
 */
@WebServlet("/schedule/import")
@MultipartConfig(maxFileSize = 1024 * 1024) // 1MB
public class ScheduleImportServlet extends HttpServlet {
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
        
        Part filePart = request.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            response.sendRedirect(request.getContextPath() + "/schedule?error=Không có file được chọn");
            return;
        }
        
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;
        
        try (InputStream inputStream = filePart.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            
            String line;
            int lineNumber = 0;
            ScheduleDAO scheduleDAO = new ScheduleDAO();
            
            // Bỏ qua dòng header
            reader.readLine();
            lineNumber++;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = parseCSVLine(line);
                if (parts.length < 4) {
                    errors.add("Dòng " + lineNumber + ": Thiếu thông tin");
                    errorCount++;
                    continue;
                }
                
                String tenMon = parts[0].trim();
                String thuTrongTuan = parts[1].trim();
                String thoiGianBatDau = parts[2].trim();
                String thoiGianKetThuc = parts[3].trim();
                String phongHoc = parts.length > 4 ? parts[4].trim() : "";
                String ghiChu = parts.length > 5 ? parts[5].trim() : "";
                
                // Validate
                if (tenMon.isEmpty() || thuTrongTuan.isEmpty() || 
                    thoiGianBatDau.isEmpty() || thoiGianKetThuc.isEmpty()) {
                    errors.add("Dòng " + lineNumber + ": Thiếu thông tin bắt buộc");
                    errorCount++;
                    continue;
                }
                
                try {
                    Time batDau = Time.valueOf(thoiGianBatDau + ":00");
                    Time ketThuc = Time.valueOf(thoiGianKetThuc + ":00");
                    
                    if (ketThuc.before(batDau) || ketThuc.equals(batDau)) {
                        errors.add("Dòng " + lineNumber + ": Thời gian không hợp lệ");
                        errorCount++;
                        continue;
                    }
                    
                    if (scheduleDAO.checkConflict(user.getId(), thuTrongTuan, batDau, ketThuc, -1)) {
                        errors.add("Dòng " + lineNumber + ": Trùng lịch học");
                        errorCount++;
                        continue;
                    }
                    
                    Schedule schedule = new Schedule();
                    schedule.setUserId(user.getId());
                    schedule.setTenMon(tenMon);
                    schedule.setThuTrongTuan(thuTrongTuan);
                    schedule.setThoiGianBatDau(batDau);
                    schedule.setThoiGianKetThuc(ketThuc);
                    schedule.setPhongHoc(phongHoc.isEmpty() ? null : phongHoc);
                    schedule.setGhiChu(ghiChu.isEmpty() ? null : ghiChu);
                    
                    if (scheduleDAO.create(schedule) > 0) {
                        successCount++;
                    } else {
                        errors.add("Dòng " + lineNumber + ": Lỗi khi lưu");
                        errorCount++;
                    }
                } catch (IllegalArgumentException e) {
                    errors.add("Dòng " + lineNumber + ": Định dạng thời gian không đúng (HH:mm)");
                    errorCount++;
                }
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/schedule?error=Lỗi khi đọc file: " + e.getMessage());
            return;
        }
        
        // Tạo message kết quả
        StringBuilder message = new StringBuilder();
        message.append("Import hoàn tất: ").append(successCount).append(" dòng thành công, ")
               .append(errorCount).append(" dòng lỗi");
        
        if (!errors.isEmpty() && errors.size() <= 10) {
            message.append(". Chi tiết: ").append(String.join("; ", errors));
        }
        
        response.sendRedirect(request.getContextPath() + "/schedule?message=" + 
            java.net.URLEncoder.encode(message.toString(), "UTF-8"));
    }
    
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        
        return result.toArray(String[]::new);
    }
}

