package com.lms.model;

import java.sql.Timestamp;

/**
 * Model class đại diện cho bảng user_profiles
 * Lưu trữ thông tin hồ sơ cá nhân của sinh viên
 */
public class UserProfile {
    private int id;
    private int userId;
    private String hoTen;
    private String chuyenNganh;
    private String namHoc;
    private Timestamp updatedAt;
    
    // Constructor mặc định
    public UserProfile() {
    }
    
    // Constructor đầy đủ
    public UserProfile(int id, int userId, String hoTen, String chuyenNganh, String namHoc, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.hoTen = hoTen;
        this.chuyenNganh = chuyenNganh;
        this.namHoc = namHoc;
        this.updatedAt = updatedAt;
    }
    
    // Constructor không có id và updatedAt (dùng khi tạo mới)
    public UserProfile(int userId, String hoTen, String chuyenNganh, String namHoc) {
        this.userId = userId;
        this.hoTen = hoTen;
        this.chuyenNganh = chuyenNganh;
        this.namHoc = namHoc;
    }
    
    // Getter và Setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public String getChuyenNganh() {
        return chuyenNganh;
    }
    
    public void setChuyenNganh(String chuyenNganh) {
        this.chuyenNganh = chuyenNganh;
    }
    
    public String getNamHoc() {
        return namHoc;
    }
    
    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", userId=" + userId +
                ", hoTen='" + hoTen + '\'' +
                ", chuyenNganh='" + chuyenNganh + '\'' +
                ", namHoc='" + namHoc + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

