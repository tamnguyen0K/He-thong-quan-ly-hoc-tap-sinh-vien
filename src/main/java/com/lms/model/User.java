package com.lms.model;

import java.sql.Timestamp;

/**
 * Model class đại diện cho bảng users
 * Lưu trữ thông tin tài khoản người dùng
 */
public class User {
    private int id;
    private String email;
    private String passwordHash;
    private String role;
    private String hoTen;
    private Timestamp createdAt;
    
    // Constructor mặc định
    public User() {
    }
    
    // Constructor đầy đủ
    public User(int id, String email, String passwordHash, String role, String hoTen, Timestamp createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.hoTen = hoTen;
        this.createdAt = createdAt;
    }
    
    // Constructor không có id và createdAt (dùng khi tạo mới)
    public User(String email, String passwordHash, String role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    
    // Constructor với hoTen (dùng khi tạo mới có tên)
    public User(String email, String passwordHash, String role, String hoTen) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.hoTen = hoTen;
    }
    
    // Getter và Setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

