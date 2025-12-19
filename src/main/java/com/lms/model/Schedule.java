package com.lms.model;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Model class đại diện cho bảng schedules
 * Lưu trữ lịch học của sinh viên
 */
public class Schedule {
    private int id;
    private int userId;
    private String tenMon;
    private String thuTrongTuan;
    private Time thoiGianBatDau;
    private Time thoiGianKetThuc;
    private String phongHoc;
    private String ghiChu;
    private Timestamp createdAt;
    
    // Constructor mặc định
    public Schedule() {
    }
    
    // Constructor đầy đủ
    public Schedule(int id, int userId, String tenMon, String thuTrongTuan, 
                   Time thoiGianBatDau, Time thoiGianKetThuc, 
                   String phongHoc, String ghiChu, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.tenMon = tenMon;
        this.thuTrongTuan = thuTrongTuan;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.phongHoc = phongHoc;
        this.ghiChu = ghiChu;
        this.createdAt = createdAt;
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
    
    public String getTenMon() {
        return tenMon;
    }
    
    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }
    
    public String getThuTrongTuan() {
        return thuTrongTuan;
    }
    
    public void setThuTrongTuan(String thuTrongTuan) {
        this.thuTrongTuan = thuTrongTuan;
    }
    
    public Time getThoiGianBatDau() {
        return thoiGianBatDau;
    }
    
    public void setThoiGianBatDau(Time thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }
    
    public Time getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }
    
    public void setThoiGianKetThuc(Time thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }
    
    public String getPhongHoc() {
        return phongHoc;
    }
    
    public void setPhongHoc(String phongHoc) {
        this.phongHoc = phongHoc;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

