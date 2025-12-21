<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.lms.model.Progress" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.sql.Timestamp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Tiến độ học tập</title>
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

        .urgent {
            background: #fff3cd;
        }

        .completed {
            opacity: 0.6;
        }
    </style>
</head>

<body>
    <div class="header">
        <h1>Hệ Thống Quản Lý Học Tập</h1>
    </div>
    <div class="container">
        <div class="card">
            <h1 style="text-align: center;">Tiến độ học tập</h1>
            <% String msg=request.getParameter("message"); %>
                <% if (msg !=null) { %>
                    <div
                        style="padding: 10px; background: #efe; color: #3c3; border-radius: 5px; margin-bottom: 20px;">
                        <%= msg %>
                    </div>
                    <% } %>

                        <% Progress editTask=(Progress) request.getAttribute("editTask"); boolean
                            isEdit=editTask !=null; %>

                            <h2>
                                <%= isEdit ? "Sửa task" : "Thêm task mới" %>
                            </h2>
                            <form method="post" action="<%= request.getContextPath() %>/progress">
                                <input type="hidden" name="action" value="<%= isEdit ? " update"
                                    : "add" %>">
                                <% if (isEdit) { %><input type="hidden" name="id"
                                        value="<%= editTask.getId() %>">
                                    <% } %>

                                        <div class="form-group">
                                            <label>Tên task *</label>
                                            <input type="text" name="tenTask" required
                                                value="<%= isEdit ? editTask.getTenTask() : "" %>">
                                        </div>
                                        <div class="form-group">
                                            <label>Loại task</label>
                                            <select name="loaiTask">
                                                <option value="">-- Chọn --</option>
                                                <option value="Bài tập" <%=isEdit && "Bài tập"
                                                    .equals(editTask.getLoaiTask()) ? "selected"
                                                    : "" %>>Bài tập</option>
                                                <option value="Thi giữa kỳ" <%=isEdit
                                                    && "Thi giữa kỳ" .equals(editTask.getLoaiTask())
                                                    ? "selected" : "" %>>Thi giữa kỳ</option>
                                                <option value="Thi cuối kỳ" <%=isEdit
                                                    && "Thi cuối kỳ" .equals(editTask.getLoaiTask())
                                                    ? "selected" : "" %>>Thi cuối kỳ</option>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Môn học</label>
                                            <input type="text" name="monHoc"
                                                value="<%= isEdit && editTask.getMonHoc() != null ? editTask.getMonHoc() : "" %>">
                                        </div>
                                        <div class="form-group">
                                            <label>Hạn nộp</label>
                                            <input type="datetime-local" name="hanNop" value="<%= isEdit && editTask.getHanNop() != null ? 
                                                                                            new java.text.SimpleDateFormat(" yyyy-MM-dd'T'HH:mm").format(editTask.getHanNop()) : "" %>">
                                        </div>
                                        <div class="form-group">
                                            <label>Mô tả</label>
                                            <textarea name="moTa"
                                                rows="3"><%= isEdit && editTask.getMoTa() != null ? editTask.getMoTa() : "" %></textarea>
                                        </div>
                                        <button type="submit" class="btn">
                                            <%= isEdit ? "Cập nhật" : "Thêm" %>
                                        </button>
                                        <% if (isEdit) { %><a
                                                href="<%= request.getContextPath() %>/progress"
                                                class="btn" style="text-decoration: none;">Hủy</a>
                                            <% } %>
                            </form>
        </div>

        <div class="card">
            <h2>Danh sách tasks</h2>
            <% List<Progress> tasks = (List<Progress>) request.getAttribute("tasks");
                    if (tasks == null || tasks.isEmpty()) {
                    %><p>Chưa có task nào.</p>
                    <% } else { Calendar cal=Calendar.getInstance(); cal.add(Calendar.DAY_OF_MONTH,
                        3); Timestamp threeDaysLater=new Timestamp(cal.getTimeInMillis()); %>
                        <table>
                            <thead>
                                <tr>
                                    <th>Tên task</th>
                                    <th>Loại</th>
                                    <th>Môn</th>
                                    <th>Hạn nộp</th>
                                    <th>Trạng thái</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Progress t : tasks) { 
                                    boolean isUrgent = t.getHanNop() != null && t.getHanNop().before(threeDaysLater) && !"daxong".equals(t.getTrangThai()); 
                                    boolean isCompleted = "daxong".equals(t.getTrangThai()); 
                                %>
                                    <tr class="<%= isUrgent ? " urgent" : "" %><%= isCompleted ? " completed" : "" %>">
                                            <td>
                                                <%= t.getTenTask() %>
                                            </td>
                                            <td>
                                                <%= t.getLoaiTask() !=null ? t.getLoaiTask() : "" %>
                                            </td>
                                            <td>
                                                <%= t.getMonHoc() !=null ? t.getMonHoc() : "" %>
                                            </td>
                                            <td>
                                                <%= t.getHanNop() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(t.getHanNop()) : "" %>
                                            </td>
                                            <td>
                                                <%= "daxong".equals(t.getTrangThai()) ? "Đã xong" : "Chưa xong" %>
                                            </td>
                                            <td>
                                                <form method="post" style="display: inline;">
                                                    <input type="hidden" name="action"
                                                        value="toggle">
                                                    <input type="hidden" name="id"
                                                        value="<%= t.getId() %>">
                                                    <button type="submit" class="btn"
                                                        style="padding: 5px 10px; font-size: 12px;">
                                                        <%= "daxong".equals(t.getTrangThai()) ? "Chưa xong" : "Đã xong" %>
                                                    </button>
                                                </form>
                                                <a href="<%= request.getContextPath() %>/progress?edit=<%= t.getId() %>"
                                                    class="btn"
                                                    style="text-decoration: none; padding: 5px 10px; font-size: 12px;">Sửa</a>
                                                <form method="post" style="display: inline;">
                                                    <input type="hidden" name="action"
                                                        value="delete">
                                                    <input type="hidden" name="id"
                                                        value="<%= t.getId() %>">
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