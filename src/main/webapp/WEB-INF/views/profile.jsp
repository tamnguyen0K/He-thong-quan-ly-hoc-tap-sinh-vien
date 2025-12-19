<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.model.User" %>
<%@ page import="com.lms.model.UserProfile" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân - Hệ Thống Quản Lý Học Tập</title>
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            text-align: center;
        }
        .container {
            max-width: 800px;
            margin: 40px auto;
            padding: 20px;
        }
        .profile-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: bold;
        }
        input[type="text"],
        select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
        }
        input[type="text"]:focus,
        select:focus {
            outline: none;
            border-color: #667eea;
        }
        .btn {
            padding: 12px 30px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: background 0.3s;
        }
        .btn:hover {
            background: #5568d3;
        }
        .error-message {
            background: #fee;
            color: #c33;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #fcc;
        }
        .success-message {
            background: #efe;
            color: #3c3;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #cfc;
        }
        .info-row {
            margin-bottom: 15px;
            padding: 10px;
            background: #f9f9f9;
            border-radius: 5px;
        }
        .info-label {
            font-weight: bold;
            color: #666;
        }
        .info-value {
            color: #333;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>Hệ Thống Quản Lý Học Tập</h1>
    </div>
    
    <div class="container">
        <div class="profile-card">
            <h1>Hồ sơ cá nhân</h1>
            
            <%-- Hiển thị thông báo thành công --%>
            <% String successMessage = (String) request.getAttribute("successMessage"); %>
            <% if (successMessage != null) { %>
                <div class="success-message" id="successMsg"><%= successMessage %></div>
            <% } %>
            
            <%-- Hiển thị thông báo lỗi --%>
            <% 
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage == null) {
                    errorMessage = request.getParameter("error");
                }
            %>
            <% if (errorMessage != null) { %>
                <div class="error-message" id="errorMsg"><%= errorMessage %></div>
            <% } %>
            
            <%
                User user = (User) request.getAttribute("user");
                UserProfile profile = (UserProfile) request.getAttribute("profile");
            %>
            
            <%-- Hiển thị thông tin hiện tại --%>
            <div class="info-row">
                <div class="info-label">Email:</div>
                <div class="info-value"><%= user != null ? user.getEmail() : "" %></div>
            </div>
            
            <% if (profile != null && profile.getHoTen() != null) { %>
                <div class="info-row">
                    <div class="info-label">Họ và tên:</div>
                    <div class="info-value"><%= profile.getHoTen() %></div>
                </div>
            <% } %>
            
            <% if (profile != null && profile.getChuyenNganh() != null) { %>
                <div class="info-row">
                    <div class="info-label">Chuyên ngành:</div>
                    <div class="info-value"><%= profile.getChuyenNganh() %></div>
                </div>
            <% } %>
            
            <% if (profile != null && profile.getNamHoc() != null) { %>
                <div class="info-row">
                    <div class="info-label">Năm học:</div>
                    <div class="info-value"><%= profile.getNamHoc() %></div>
                </div>
            <% } %>
            
            <hr style="margin: 30px 0;">
            
            <%-- Form cập nhật --%>
            <form method="post" action="<%= request.getContextPath() %>/profile">
                <div class="form-group">
                    <label for="hoTen">Họ và tên:</label>
                    <input type="text" id="hoTen" name="hoTen" 
                           value="<%= profile != null && profile.getHoTen() != null ? profile.getHoTen() : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="chuyenNganh">Chuyên ngành:</label>
                    <input type="text" id="chuyenNganh" name="chuyenNganh" 
                           value="<%= profile != null && profile.getChuyenNganh() != null ? profile.getChuyenNganh() : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="namHoc">Năm học:</label>
                    <select id="namHoc" name="namHoc">
                        <option value="">-- Chọn năm học --</option>
                        <option value="Năm 1" <%= profile != null && "Năm 1".equals(profile.getNamHoc()) ? "selected" : "" %>>Năm 1</option>
                        <option value="Năm 2" <%= profile != null && "Năm 2".equals(profile.getNamHoc()) ? "selected" : "" %>>Năm 2</option>
                        <option value="Năm 3" <%= profile != null && "Năm 3".equals(profile.getNamHoc()) ? "selected" : "" %>>Năm 3</option>
                        <option value="Năm 4" <%= profile != null && "Năm 4".equals(profile.getNamHoc()) ? "selected" : "" %>>Năm 4</option>
                    </select>
                </div>
                
                <button type="submit" class="btn" id="submitBtn">Cập nhật</button>
            </form>
            
            <div style="margin-top: 20px;">
                <a href="<%= request.getContextPath() %>/dashboard" style="color: #667eea; text-decoration: none;">← Quay lại Dashboard</a>
            </div>
        </div>
    </div>
    
    <script>
        // Auto-hide success message sau 3 giây
        var successMsg = document.getElementById('successMsg');
        if (successMsg) {
            setTimeout(function() {
                successMsg.style.transition = 'opacity 0.5s';
                successMsg.style.opacity = '0';
                setTimeout(function() {
                    successMsg.remove();
                }, 500);
            }, 3000);
        }
        
        // Auto-hide error message sau 5 giây
        var errorMsg = document.getElementById('errorMsg');
        if (errorMsg) {
            setTimeout(function() {
                errorMsg.style.transition = 'opacity 0.5s';
                errorMsg.style.opacity = '0';
                setTimeout(function() {
                    errorMsg.remove();
                }, 500);
            }, 5000);
        }
        
        // Disable button sau khi submit để tránh double submit
        var form = document.querySelector('form');
        if (form) {
            form.addEventListener('submit', function() {
                var submitBtn = document.getElementById('submitBtn');
                if (submitBtn) {
                    submitBtn.disabled = true;
                    submitBtn.textContent = 'Đang xử lý...';
                }
            });
        }
    </script>
</body>
</html>

