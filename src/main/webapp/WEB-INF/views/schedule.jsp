<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.lms.model.Schedule" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch học - Hệ Thống Quản Lý Học Tập</title>
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

        .card h1{
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
            font-size: 14px;
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

        .btn-danger:hover {
            background: #c82333;
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
            font-weight: bold;
        }

        .message {
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .success {
            background: #efe;
            color: #3c3;
            border: 1px solid #cfc;
        }

        .error {
            background: #fee;
            color: #c33;
            border: 1px solid #fcc;
        }
    </style>
</head>

<body>
    <div class="header">
        <h1>Hệ Thống Quản Lý Học Tập</h1>
    </div>
    <div class="container">
        <div class="card">
            <h1 style="text-align: center;">Lịch học</h1>

            <% String message=request.getParameter("message"); %>
                <% String error=request.getParameter("error"); %>
                    <% if (message !=null) { %>
                        <div class="message success">
                            <%= message %>
                        </div>
                        <% } %>
                            <% if (error !=null) { %>
                                <div class="message error">
                                    <%= error %>
                                </div>
                                <% } %>

                                    <% Schedule editSchedule=(Schedule)
                                        request.getAttribute("editSchedule"); boolean isEdit=editSchedule
                                        !=null; %>

                                        <h2>
                                            <%= isEdit ? "Sửa lịch học" : "Thêm lịch học mới" %>
                                        </h2>
                                        <form method="post"
                                            action="<%= request.getContextPath() %>/schedule">
                                            <input type="hidden" name="action" value="<%= isEdit ? " update"
                                                : "add" %>">
                                            <% if (isEdit) { %>
                                                <input type="hidden" name="id"
                                                    value="<%= editSchedule.getId() %>">
                                                <% } %>

                                                    <div class="form-group">
                                                        <label>Tên môn học *</label>
                                                        <input type="text" name="tenMon" required
                                                            value="<%= isEdit ? editSchedule.getTenMon() : "" %>">
                                                    </div>

                                                    <div class="form-group">
                                                        <label>Thứ trong tuần *</label>
                                                        <select name="thuTrongTuan" required>
                                                            <option value="">-- Chọn thứ --</option>
                                                            <option value="Thứ 2" <%=isEdit && "Thứ 2"
                                                                .equals(editSchedule.getThuTrongTuan())
                                                                ? "selected" : "" %>>Thứ 2</option>
                                                            <option value="Thứ 3" <%=isEdit && "Thứ 3"
                                                                .equals(editSchedule.getThuTrongTuan())
                                                                ? "selected" : "" %>>Thứ 3</option>
                                                            <option value="Thứ 4" <%=isEdit && "Thứ 4"
                                                                .equals(editSchedule.getThuTrongTuan())
                                                                ? "selected" : "" %>>Thứ 4</option>
                                                            <option value="Thứ 5" <%=isEdit && "Thứ 5"
                                                                .equals(editSchedule.getThuTrongTuan())
                                                                ? "selected" : "" %>>Thứ 5</option>
                                                            <option value="Thứ 6" <%=isEdit && "Thứ 6"
                                                                .equals(editSchedule.getThuTrongTuan())
                                                                ? "selected" : "" %>>Thứ 6</option>
                                                            <option value="Thứ 7" <%=isEdit && "Thứ 7"
                                                                .equals(editSchedule.getThuTrongTuan())
                                                                ? "selected" : "" %>>Thứ 7</option>
                                                            <option value="Chủ nhật" <%=isEdit && "Chủ nhật"
                                                                .equals(editSchedule.getThuTrongTuan())
                                                                ? "selected" : "" %>>Chủ nhật</option>
                                                        </select>
                                                    </div>

                                                    <div class="form-group">
                                                        <label>Thời gian bắt đầu *</label>
                                                        <input type="time" name="thoiGianBatDau" required
                                                            value="<%= isEdit ? editSchedule.getThoiGianBatDau().toString().substring(0,5) : "" %>">
                                                    </div>

                                                    <div class="form-group">
                                                        <label>Thời gian kết thúc *</label>
                                                        <input type="time" name="thoiGianKetThuc" required
                                                            value="<%= isEdit ? editSchedule.getThoiGianKetThuc().toString().substring(0,5) : "" %>">
                                                    </div>

                                                    <div class="form-group">
                                                        <label>Phòng học</label>
                                                        <input type="text" name="phongHoc"
                                                            value="<%= isEdit && editSchedule.getPhongHoc() != null ? editSchedule.getPhongHoc() : "" %>">
                                                    </div>

                                                    <div class="form-group">
                                                        <label>Ghi chú</label>
                                                        <textarea name="ghiChu"
                                                            rows="3"><%= isEdit && editSchedule.getGhiChu() != null ? editSchedule.getGhiChu() : "" %></textarea>
                                                    </div>

                                                    <button type="submit" class="btn">
                                                        <%= isEdit ? "Cập nhật" : "Thêm" %>
                                                    </button>
                                                    <% if (isEdit) { %>
                                                        <a href="<%= request.getContextPath() %>/schedule"
                                                            class="btn"
                                                            style="text-decoration: none; display: inline-block;">Hủy</a>
                                                        <% } %>
                                        </form>
        </div>

        <div class="card">
            <h2>Import từ CSV</h2>
            <form method="post" action="<%= request.getContextPath() %>/schedule/import"
                enctype="multipart/form-data">
                <div class="form-group">
                    <label>Chọn file CSV</label>
                    <input type="file" name="file" accept=".csv" required>
                </div>
                <button type="submit" class="btn">Import CSV</button>
            </form>
        </div>

        <div class="card">
            <h2 style="text-align: center;">Danh sách lịch học</h2>
            <% List<Schedule> schedules = (List<Schedule>) request.getAttribute("schedules");
                    if (schedules == null || schedules.isEmpty()) {
                    %>
                    <p>Chưa có lịch học nào.</p>
                    <% } else { %>
                        <table>
                            <thead>
                                <tr>
                                    <th>Tên môn</th>
                                    <th>Thứ</th>
                                    <th>Thời gian</th>
                                    <th>Phòng</th>
                                    <th>Ghi chú</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Schedule s : schedules) { %>
                                    <tr>
                                        <td>
                                            <%= s.getTenMon() %>
                                        </td>
                                        <td>
                                            <%= s.getThuTrongTuan() %>
                                        </td>
                                        <td>
                                            <%= s.getThoiGianBatDau().toString().substring(0,5) %> - <%=
                                                    s.getThoiGianKetThuc().toString().substring(0,5) %>
                                        </td>
                                        <td>
                                            <%= s.getPhongHoc() !=null ? s.getPhongHoc() : "" %>
                                        </td>
                                        <td>
                                            <%= s.getGhiChu() !=null ? s.getGhiChu() : "" %>
                                        </td>
                                        <td>
                                            <a href="<%= request.getContextPath() %>/schedule?edit=<%= s.getId() %>"
                                                class="btn"
                                                style="text-decoration: none; padding: 5px 10px; font-size: 12px;">Sửa</a>
                                            <form method="post"
                                                action="<%= request.getContextPath() %>/schedule"
                                                style="display: inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="<%= s.getId() %>">
                                                <button type="submit" class="btn btn-danger"
                                                    style="padding: 5px 10px; font-size: 12px;"
                                                    onclick="return confirm('Bạn có chắc muốn xóa?')">Xóa</button>
                                            </form>
                                        </td>
                                    </tr>
                                    <% } %>
                            </tbody>
                        </table>
                        <% } %>
        </div>

        <div style="margin-top: 20px;">
            <a href="<%= request.getContextPath() %>/dashboard"
                style="color: #667eea; text-decoration: none;">← Quay lại Dashboard</a>
        </div>
    </div>
</body>

</html>