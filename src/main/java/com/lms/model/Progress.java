package com.lms.model;

import java.sql.Timestamp;

/**
 * Model class đại diện cho bảng progress
 * Lưu trữ tiến độ học tập (các task)
 */
public class Progress {
    private int id;
    private int userId;
    private String tenTask;
    private String loaiTask;
    private String monHoc;
    private String trangThai;
    private Timestamp hanNop;
    private String moTa;
    private Timestamp createdAt;
    private Timestamp completedAt;
    
    public Progress() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTenTask() { return tenTask; }
    public void setTenTask(String tenTask) { this.tenTask = tenTask; }
    public String getLoaiTask() { return loaiTask; }
    public void setLoaiTask(String loaiTask) { this.loaiTask = loaiTask; }
    public String getMonHoc() { return monHoc; }
    public void setMonHoc(String monHoc) { this.monHoc = monHoc; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public Timestamp getHanNop() { return hanNop; }
    public void setHanNop(Timestamp hanNop) { this.hanNop = hanNop; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getCompletedAt() { return completedAt; }
    public void setCompletedAt(Timestamp completedAt) { this.completedAt = completedAt; }
}

