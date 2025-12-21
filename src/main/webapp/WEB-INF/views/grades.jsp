<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.lms.model.Grade" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Điểm số</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
        }

        .header {
            background:#667eea;
            color: white;
            padding: 20px;
            text-align: center;
        }

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 20px;
        }

        .card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        .card h1,
        h2 {
            color: #333;
            margin-bottom: 20px;
        }

        .gpa-display {
            background: #e3f2fd;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: bold;
        }

        input,
        select,
        textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .btn {
            padding: 10px 20px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .btn:hover {
            background: #5568d3;
        }

        .btn-danger {
            background: #dc3545;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th,
        td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background: #f8f9fa;
        }
    </style>
</head>

<body>
    <div class="header">
        <h1>Hệ Thống Quản Lý Học Tập</h1>
    </div>
    <div class="container">
        <div class="card">
            <h1 style="text-align: center;">Điểm số</h1>

            <div class="gpa-display">
                <strong>GPA tổng hệ 10: <%= request.getAttribute("overallGPA10") != null ? String.format("%.2f", request.getAttribute("overallGPA10")) : "0.00" %></strong>
                <br><strong>GPA tổng hệ 4: <%= request.getAttribute("overallGPA4") != null ? String.format("%.2f", request.getAttribute("overallGPA4")) : "0.00" %></strong>
            </div>

            <% String msg=request.getParameter("message"); %>
                <% if (msg !=null) { %>
                    <div
                        style="padding: 10px; background: #efe; color: #3c3; border-radius: 5px; margin-bottom: 20px;">
                        <%= msg %>
                    </div>
                    <% } %>

                        <form method="get" style="margin-bottom: 20px;">
                            <select name="term" onchange="this.form.submit()">
                                <option value="">Tất cả học kỳ</option>
                                <% Set<String> terms = (Set<String>) request.getAttribute("terms");
                                        String selectedTerm = (String)
                                        request.getAttribute("selectedTerm");
                                        if (terms != null) {
                                        for (String t : terms) {
                                        %>
                                        <option value="<%= t %>" <%=t.equals(selectedTerm) ? "selected"
                                            : "" %>><%= t %>
                                        </option>
                                        <% } } %>
                            </select>
                        </form>

                        <% Grade editGrade=(Grade) request.getAttribute("editGrade"); boolean
                            isEdit=editGrade !=null; %>

                            <h2>
                                <%= isEdit ? "Sửa điểm" : "Thêm điểm mới" %>
                            </h2>
                            <form method="post" action="<%= request.getContextPath() %>/grades" id="gradeForm">
                                <input type="hidden" name="action" value="<%= isEdit ? "update" : "add" %>">
                                <% if (isEdit) { %><input type="hidden" name="id" value="<%= editGrade.getId() %>"><% } %>

                                        <div class="form-group">
                                            <label>Tên môn *</label>
                                            <% if (!isEdit) { %>
                                                <select id="tenMonSelect" onchange="handleSubjectChange()">
                                                    <option value="">-- Chọn môn học đã có --</option>
                                                    <% List<Grade> subjects = (List<Grade>) request.getAttribute("subjects");
                                                       if (subjects != null) {
                                                           for (Grade s : subjects) { %>
                                                                <option value="<%= s.getTenMon() %>" 
                                                                        data-namhoc="<%= s.getNamHoc() != null ? s.getNamHoc() : "" %>"
                                                                        data-hocky="<%= s.getHocKy() != null ? s.getHocKy() : "" %>"
                                                                        data-tinchi="<%= s.getTinChi() %>">
                                                                    <%= s.getTenMon() %> (<%= s.getNamHoc() != null ? s.getNamHoc() : "" %> - <%= s.getHocKy() %>)
                                                                </option>
                                                    <%     }
                                                       } %>
                                                </select>
                                                <div style="margin: 5px 0; text-align: center; color: #666;">hoặc</div>
                                                <input type="text" name="tenMon" id="tenMonInput" required
                                                       placeholder="Nhập tên môn mới" 
                                                       oninput="handleNewSubjectInput()">
                                            <% } else { %>
                                                <input type="text" name="tenMon" readonly value="<%= editGrade.getTenMon() %>">
                                            <% } %>
                                        </div>
                                        <div class="form-group">
                                            <label>Năm học *</label>
                                            <input type="text" name="namHoc" id="namHoc" required
                                                value="<%= isEdit && editGrade.getNamHoc() != null ? editGrade.getNamHoc() : "" %>"
                                                placeholder="VD: 2024-2025">
                                        </div>
                                        <div class="form-group">
                                            <label>Học kỳ *</label>
                                            <input type="text" name="hocKy" id="hocKy" required
                                                value="<%= isEdit ? editGrade.getHocKy() : "" %>"
                                                placeholder="VD: HK1, HK2">
                                        </div>
                                        <div class="form-group">
                                            <label>Số tín chỉ *</label>
                                            <input type="number" name="tinChi" id="tinChi" min="1" required
                                                value="<%= isEdit ? editGrade.getTinChi() : "" %>">
                                        </div>
                                        <div class="form-group">
                                            <label>Điểm số *</label>
                                            <input type="number" name="diemSo" min="0" max="10"
                                                step="0.01" required
                                                value="<%= isEdit ? editGrade.getDiemSo() : "" %>">
                                        </div>
                                        <div class="form-group">
                                            <label>Loại điểm *</label>
                                            <select name="loaiDiem" required>
                                                <option value="">-- Chọn loại điểm --</option>
                                                <option value="quatrinh" <%= isEdit && "quatrinh".equals(editGrade.getLoaiDiem()) ? "selected" : "" %>>Điểm quá trình</option>
                                                <option value="giuaky" <%= isEdit && "giuaky".equals(editGrade.getLoaiDiem()) ? "selected" : "" %>>Điểm giữa kỳ</option>
                                                <option value="ketthuc" <%= isEdit && "ketthuc".equals(editGrade.getLoaiDiem()) ? "selected" : "" %>>Điểm kết thúc</option>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Ghi chú</label>
                                            <textarea name="ghiChu" rows="3"><%= isEdit && editGrade.getGhiChu() != null ? editGrade.getGhiChu() : "" %></textarea>
                                        </div>
                                        <button type="submit" class="btn">
                                            <%= isEdit ? "Cập nhật" : "Thêm" %>
                                        </button>
                                        <% if (isEdit) { %><a href="<%= request.getContextPath() %>/grades" class="btn" style="text-decoration: none;">Hủy</a><% } %>
                            </form>
                            
                            <script>
                                function handleSubjectChange() {
                                    var select = document.getElementById('tenMonSelect');
                                    var input = document.getElementById('tenMonInput');
                                    var selectedOption = select.options[select.selectedIndex];
                                    
                                    if (selectedOption.value !== '') {
                                        // Đã chọn môn từ dropdown - tự động điền
                                        input.value = selectedOption.value;
                                        document.getElementById('namHoc').value = selectedOption.getAttribute('data-namhoc') || '';
                                        document.getElementById('hocKy').value = selectedOption.getAttribute('data-hocky') || '';
                                        document.getElementById('tinChi').value = selectedOption.getAttribute('data-tinchi') || '';
                                    } else {
                                        // Chọn "nhập mới" - xóa giá trị đã điền (nhưng không bắt buộc phải xóa input)
                                        document.getElementById('namHoc').value = '';
                                        document.getElementById('hocKy').value = '';
                                        document.getElementById('tinChi').value = '';
                                    }
                                }
                                
                                function handleNewSubjectInput() {
                                    var select = document.getElementById('tenMonSelect');
                                    
                                    // Nếu đang nhập môn mới, reset dropdown về "nhập mới"
                                    if (select.value !== '') {
                                        select.value = '';
                                    }
                                }
                            </script>
        </div>

        <div class="card">
            <h2>Bảng điểm chi tiết</h2>
            <% List<Grade> grades = (List<Grade>) request.getAttribute("grades");
               if (grades == null || grades.isEmpty()) { %>
                <p>Chưa có điểm nào.</p>
            <% } else { %>
                <table>
                    <thead>
                        <tr>
                            <th>Tên môn</th>
                            <th>Năm học</th>
                            <th>Học kỳ</th>
                            <th>Tín chỉ</th>
                            <th>Điểm</th>
                            <th>Loại điểm</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Grade g : grades) { %>
                            <tr>
                                <td><%= g.getTenMon() %></td>
                                <td><%= g.getNamHoc() != null ? g.getNamHoc() : "" %></td>
                                <td><%= g.getHocKy() %></td>
                                <td><%= g.getTinChi() %></td>
                                <td><%= String.format("%.2f", g.getDiemSo()) %></td>
                                <td>
                                    <% 
                                        String loaiDiemName = "";
                                        if ("quatrinh".equals(g.getLoaiDiem())) loaiDiemName = "Quá trình";
                                        else if ("giuaky".equals(g.getLoaiDiem())) loaiDiemName = "Giữa kỳ";
                                        else if ("ketthuc".equals(g.getLoaiDiem())) loaiDiemName = "Kết thúc";
                                    %>
                                    <%= loaiDiemName %>
                                </td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/grades?edit=<%= g.getId() %>" class="btn" style="text-decoration: none; padding: 5px 10px; font-size: 12px;">Sửa</a>
                                    <form method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="<%= g.getId() %>">
                                        <button type="submit" class="btn btn-danger" style="padding: 5px 10px; font-size: 12px;" onclick="return confirm('Xóa?')">Xóa</button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>

        <div class="card">
            <h2>Bảng điểm tổng kết</h2>
            <% List<Grade> finalGrades = (List<Grade>) request.getAttribute("finalGrades");
               if (finalGrades == null || finalGrades.isEmpty()) { %>
                <p>Chưa có môn học nào có đủ 3 loại điểm để tính tổng kết.</p>
            <% } else { %>
                <table>
                    <thead>
                        <tr>
                            <th>Tên môn</th>
                            <th>Năm học</th>
                            <th>Học kỳ</th>
                            <th>Tín chỉ</th>
                            <th>Điểm tổng kết (hệ 10)</th>
                            <th>Điểm hệ 4</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                           Map<Integer, Double> grade4Map = (Map<Integer, Double>) request.getAttribute("grade4Map");
                           if (grade4Map == null) grade4Map = new java.util.HashMap<>();
                           for (Grade g : finalGrades) { 
                               Double diem4 = grade4Map.get(g.getId());
                               if (diem4 == null) diem4 = 0.0;
                        %>
                            <tr>
                                <td><%= g.getTenMon() %></td>
                                <td><%= g.getNamHoc() != null ? g.getNamHoc() : "" %></td>
                                <td><%= g.getHocKy() %></td>
                                <td><%= g.getTinChi() %></td>
                                <td><strong><%= String.format("%.2f", g.getDiemSo()) %></strong></td>
                                <td><strong><%= String.format("%.2f", diem4) %></strong></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>
        <div style="margin-top: 20px;"><a href="<%= request.getContextPath() %>/dashboard"
                style="color: #667eea;">← Dashboard</a></div>
    </div>
</body>

</html>