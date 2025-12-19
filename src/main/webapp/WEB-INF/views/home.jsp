<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Hệ Thống Quản Lý Học Tập</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .container {
            text-align: center;
            background: white;
            padding: 50px;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            max-width: 500px;
            width: 90%;
        }
        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 2.5em;
        }
        p {
            color: #666;
            font-size: 18px;
            margin-bottom: 30px;
        }
        .btn-group {
            display: flex;
            flex-direction: column;
            gap: 15px;
            margin-top: 30px;
        }
        .btn {
            padding: 15px 30px;
            font-size: 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
            font-weight: bold;
        }
        .btn-primary {
            background: #667eea;
            color: white;
        }
        .btn-primary:hover {
            background: #5568d3;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        .btn-secondary {
            background: #48bb78;
            color: white;
        }
        .btn-secondary:hover {
            background: #38a169;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(72, 187, 120, 0.4);
        }
        .btn-outline {
            background: transparent;
            color: #667eea;
            border: 2px solid #667eea;
        }
        .btn-outline:hover {
            background: #667eea;
            color: white;
        }
        .user-info {
            background: #e3f2fd;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .user-info p {
            margin: 5px 0;
            color: #1976d2;
        }
        @media (max-width: 600px) {
            .container {
                padding: 30px 20px;
            }
            h1 {
                font-size: 2em;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Hello LMS</h1>
        <p>Hệ Thống Quản Lý Học Tập</p>
        
        <%
            // Kiểm tra xem user đã đăng nhập chưa
            User user = (User) session.getAttribute("user");
            String contextPath = request.getContextPath();
        %>
        
        <% if (user != null) { %>
            <!-- Nếu đã đăng nhập -->
            <div class="user-info">
                <p><strong>Xin chào, <%= user.getEmail() %>!</strong></p>
                <p>Bạn đã đăng nhập vào hệ thống</p>
            </div>
            <div class="btn-group">
                <a href="<%= contextPath %>/dashboard" class="btn btn-primary">Vào Dashboard</a>
                <a href="<%= contextPath %>/logout" class="btn btn-outline">Đăng xuất</a>
            </div>
        <% } else { %>
            <!-- Nếu chưa đăng nhập -->
            <div class="btn-group">
                <a href="<%= contextPath %>/login" class="btn btn-primary">Đăng nhập</a>
                <a href="<%= contextPath %>/register" class="btn btn-secondary">Đăng ký</a>
            </div>
        <% } %>
    </div>
</body>
</html>

