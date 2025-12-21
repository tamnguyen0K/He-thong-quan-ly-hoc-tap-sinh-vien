<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.lms.model.Document" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Tài liệu học tập</title>
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

        .card h1 {
        text-align: center;
        }
        
        .card h1,
        h2 {
            color: #333;
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
            <h1>Tài liệu học tập</h1>

            <% String msg=request.getParameter("message"); %>
                <% String error=request.getParameter("error"); %>
                    <% if (msg !=null) { %>
                        <div
                            style="padding: 10px; background: #efe; color: #3c3; border-radius: 5px; margin-bottom: 20px;">
                            <%= msg %>
                        </div>
                        <% } %>
                            <% if (error !=null) { %>
                                <div
                                    style="padding: 10px; background: #fee; color: #c33; border-radius: 5px; margin-bottom: 20px;">
                                    <%= error %>
                                </div>
                                <% } %>

                                    <h2>Upload tài liệu</h2>
                                    <form method="post"
                                        action="<%= request.getContextPath() %>/documents/upload"
                                        enctype="multipart/form-data">
                                        <div class="form-group">
                                            <label>Chọn file (DOC/DOCX/TXT, tối đa 10MB)
                                                *</label>
                                            <input type="file" name="file"
                                                accept=".doc,.docx,.txt" required>
                                        </div>
                                        <div class="form-group">
                                            <label>Môn học</label>
                                            <input type="text" name="monHoc">
                                        </div>
                                        <div class="form-group">
                                            <label>Mô tả</label>
                                            <textarea name="moTa" rows="3"></textarea>
                                        </div>
                                        <button type="submit" class="btn">Upload</button>
                                    </form>
        </div>

        <div class="card">
            <h2 style="text-align: center;">Danh sách tài liệu</h2>
            <form method="get" style="margin-bottom: 20px;">
                <select name="monHoc" onchange="this.form.submit()">
                    <option value="">Tất cả môn học</option>
                    <% List<Document> allDocs = (List<Document>) request.getAttribute("documents");
                            Set<String> monHocs = new HashSet<>();
                                    if (allDocs != null) {
                                    for (Document d : allDocs) {
                                    if (d.getMonHoc() != null && !d.getMonHoc().isEmpty()) {
                                    monHocs.add(d.getMonHoc());
                                    }
                                    }
                                    String selectedMonHoc = (String)
                                    request.getAttribute("selectedMonHoc");
                                    for (String mh : monHocs) {
                                    %>
                                    <option value="<%= mh %>" <%=mh.equals(selectedMonHoc)
                                        ? "selected" : "" %>><%= mh %>
                                    </option>
                                    <% } } %>
                </select>
            </form>

            <% List<Document> documents = (List<Document>) request.getAttribute("documents");
                    if (documents == null || documents.isEmpty()) {
                    %><p>Chưa có tài liệu nào.</p>
                    <% } else { %>
                        <table>
                            <thead>
                                <tr>
                                    <th>Tên file</th>
                                    <th>Môn học</th>
                                    <th>Loại</th>
                                    <th>Kích thước</th>
                                    <th>Ngày upload</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Document d : documents) { %>
                                    <tr>
                                        <td>
                                            <%= d.getTenFileGoc() %>
                                        </td>
                                        <td>
                                            <%= d.getMonHoc() !=null ? d.getMonHoc() : "" %>
                                        </td>
                                        <td>
                                            <%= d.getLoaiFile() !=null ?
                                                d.getLoaiFile().toUpperCase() : "" %>
                                        </td>
                                        <td>
                                            <%= com.lms.util.FileUtil.formatFileSize(d.getKichThuoc())
                                                %>
                                        </td>
                                        <td>
                                            <%= d.getUploadedAt() !=null ? new
                                                java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(d.getUploadedAt()) : "" %>
                                        </td>
                                        <td>
                                            <a href="<%= request.getContextPath() %>/documents/download?id=<%= d.getId() %>"
                                                class="btn"
                                                style="text-decoration: none; padding: 5px 10px; font-size: 12px;">Download</a>
                                            <form method="post" style="display: inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id"
                                                    value="<%= d.getId() %>">
                                                <button type="submit" class="btn btn-danger"
                                                    style="padding: 5px 10px; font-size: 12px;"
                                                    onclick="return confirm('Xóa?')">Xóa</button>
                                            </form>
                                        </td>
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