<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Hệ Thống Quản Lý Học Tập</title>
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
        .welcome-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        .welcome-card h1 {
            color: #333;
            margin-bottom: 10px;
        }
        .welcome-card p {
            color: #ffffff;
            font-size: 18px;
        }
        .logout-btn {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background: #dc3545;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background 0.3s;
        }
        .logout-btn:hover {
            background: #c82333;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>Chào Mừng Đến Hệ Thống Quản Lý Học Tập</h1>
    </div>
    
    <div class="container">
        <div class="welcome-card" style="margin-top: 20px; text-align: left;">
            <h2 style="text-align: center; margin-bottom: 30px;">Menu điều hướng</h2>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px;">
                <a href="<%= request.getContextPath() %>/profile" style="display: block; padding: 20px; background: #667eea; color: white; text-decoration: none; border-radius: 10px; text-align: center; transition: transform 0.2s;">
                    <h3>📋 Hồ sơ cá nhân</h3>
                    <p style="font-size: 14px; margin-top: 10px;">Xem và cập nhật thông tin</p>
                </a>
                <a href="<%= request.getContextPath() %>/schedule" style="display: block; padding: 20px; background: #48bb78; color: white; text-decoration: none; border-radius: 10px; text-align: center; transition: transform 0.2s;">
                    <h3>📅 Lịch học</h3>
                    <p style="font-size: 14px; margin-top: 10px;">Quản lý lịch học</p>
                </a>
                <a href="<%= request.getContextPath() %>/progress" style="display: block; padding: 20px; background: #ed8936; color: white; text-decoration: none; border-radius: 10px; text-align: center; transition: transform 0.2s;">
                    <h3>✅ Tiến độ học tập</h3>
                    <p style="font-size: 14px; margin-top: 10px;">Theo dõi task</p>
                </a>
                <a href="<%= request.getContextPath() %>/grades" style="display: block; padding: 20px; background: #9f7aea; color: white; text-decoration: none; border-radius: 10px; text-align: center; transition: transform 0.2s;">
                    <h3>📊 Điểm số & GPA</h3>
                    <p style="font-size: 14px; margin-top: 10px;">Quản lý điểm và GPA</p>
                </a>
                <a href="<%= request.getContextPath() %>/documents" style="display: block; padding: 20px; background: #4299e1; color: white; text-decoration: none; border-radius: 10px; text-align: center; transition: transform 0.2s;">
                    <h3>📁 Tài liệu học tập</h3>
                    <p style="font-size: 14px; margin-top: 10px;">Upload và quản lý tài liệu</p>
                </a>
            </div>
            <div style="text-align: center; margin-top: 30px;">
                <a href="<%= request.getContextPath() %>/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </div>
    </div>
</body>
</html>

