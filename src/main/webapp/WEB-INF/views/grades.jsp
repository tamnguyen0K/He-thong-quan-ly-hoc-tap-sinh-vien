<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.model.Grade" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Điểm số</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f5f5f5; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; text-align: center; }
        .container { max-width: 1200px; margin: 40px auto; padding: 20px; }
        .card { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); margin-bottom: 20px; }
        h1, h2 { color: #333; margin-bottom: 20px; }
        .gpa-display { background: #e3f2fd; padding: 15px; border-radius: 5px; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; color: #555; font-weight: bold; }
        input, select, textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
        .btn { padding: 10px 20px; background: #667eea; color: white; border: none; border-radius: 5px; cursor: pointer; }
        .btn:hover { background: #5568d3; }
        .btn-danger { background: #dc3545; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f8f9fa; }
    </style>
</head>
<body>
    <div class="header"><h1>Hệ Thống Quản Lý Học Tập</h1></div>
    <div class="container">
        <div class="card">
            <h1>Điểm số</h1>
            
            <div class="gpa-display">
                <strong>GPA tổng: <%= request.getAttribute("overallGPA") != null ? 
                    String.format("%.2f", request.getAttribute("overallGPA")) : "0.00" %></strong>
                <% if (request.getAttribute("termGPA") != null) { %>
                    <br><strong>GPA học kỳ: <%= String.format("%.2f", request.getAttribute("termGPA")) %></strong>
                <% } %>
            </div>
            
            <% String msg = request.getParameter("message"); %>
            <% if (msg != null) { %><div style="padding: 10px; background: #efe; color: #3c3; border-radius: 5px; margin-bottom: 20px;"><%= msg %></div><% } %>
            
            <form method="get" style="margin-bottom: 20px;">
                <select name="term" onchange="this.form.submit()">
                    <option value="">Tất cả học kỳ</option>
                    <% Set<String> terms = (Set<String>) request.getAttribute("terms");
                       String selectedTerm = (String) request.getAttribute("selectedTerm");
                       if (terms != null) {
                           for (String t : terms) {
                    %>
                    <option value="<%= t %>" <%= t.equals(selectedTerm) ? "selected" : "" %>><%= t %></option>
                    <% } } %>
                </select>
            </form>
            
            <%
                Grade editGrade = (Grade) request.getAttribute("editGrade");
                boolean isEdit = editGrade != null;
            %>
            
            <h2><%= isEdit ? "Sửa điểm" : "Thêm điểm mới" %></h2>
            <form method="post" action="<%= request.getContextPath() %>/grades">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "add" %>">
                <% if (isEdit) { %><input type="hidden" name="id" value="<%= editGrade.getId() %>"><% } %>
                
                <div class="form-group">
                    <label>Tên môn *</label>
                    <input type="text" name="tenMon" required value="<%= isEdit ? editGrade.getTenMon() : "" %>">
                </div>
                <div class="form-group">
                    <label>Học kỳ *</label>
                    <input type="text" name="hocKy" required value="<%= isEdit ? editGrade.getHocKy() : "" %>">
                </div>
                <div class="form-group">
                    <label>Số tín chỉ *</label>
                    <input type="number" name="tinChi" min="1" required value="<%= isEdit ? editGrade.getTinChi() : "" %>">
                </div>
                <div class="form-group">
                    <label>Điểm số *</label>
                    <input type="number" name="diemSo" min="0" max="10" step="0.01" required 
                           value="<%= isEdit ? editGrade.getDiemSo() : "" %>">
                </div>
                <div class="form-group">
                    <label>Loại điểm</label>
                    <select name="loaiDiem">
                        <option value="">-- Chọn --</option>
                        <option value="quatrinh" <%= isEdit && "quatrinh".equals(editGrade.getLoaiDiem()) ? "selected" : "" %>>Điểm quá trình</option>
                        <option value="giuaky" <%= isEdit && "giuaky".equals(editGrade.getLoaiDiem()) ? "selected" : "" %>>Điểm giữa kỳ</option>
                        <option value="cuoiky" <%= isEdit && "cuoiky".equals(editGrade.getLoaiDiem()) ? "selected" : "" %>>Điểm cuối kỳ</option>
                        <option value="tongket" <%= isEdit && "tongket".equals(editGrade.getLoaiDiem()) ? "selected" : "" %>>Điểm tổng kết</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Ghi chú</label>
                    <textarea name="ghiChu" rows="3"><%= isEdit && editGrade.getGhiChu() != null ? editGrade.getGhiChu() : "" %></textarea>
                </div>
                <button type="submit" class="btn"><%= isEdit ? "Cập nhật" : "Thêm" %></button>
                <% if (isEdit) { %><a href="<%= request.getContextPath() %>/grades" class="btn" style="text-decoration: none;">Hủy</a><% } %>
            </form>
        </div>
        
        <div class="card">
            <h2>Bảng điểm</h2>
            <%
                List<Grade> grades = (List<Grade>) request.getAttribute("grades");
                if (grades == null || grades.isEmpty()) {
            %><p>Chưa có điểm nào.</p><%
                } else {
            %>
            <table>
                <thead><tr><th>Tên môn</th><th>Học kỳ</th><th>Tín chỉ</th><th>Điểm</th><th>Loại</th><th>Thao tác</th></tr></thead>
                <tbody>
                    <% for (Grade g : grades) { %>
                    <tr>
                        <td><%= g.getTenMon() %></td>
                        <td><%= g.getHocKy() %></td>
                        <td><%= g.getTinChi() %></td>
                        <td><%= g.getDiemSo() %></td>
                        <td><%= g.getLoaiDiem() != null ? g.getLoaiDiem() : "" %></td>
                        <td>
                            <a href="<%= request.getContextPath() %>/grades?edit=<%= g.getId() %>" class="btn" style="text-decoration: none; padding: 5px 10px; font-size: 12px;">Sửa</a>
                            <form method="post" style="display: inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= g.getId() %>">
                                <button type="submit" class="btn btn-danger" style="padding: 5px 10px; font-size: 12px;" 
                                        onclick="return confirm('Xóa?')">Xóa</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <% } %>
        </div>
        <div style="margin-top: 20px;"><a href="<%= request.getContextPath() %>/dashboard" style="color: #667eea;">← Dashboard</a></div>
    </div>
</body>
</html>

