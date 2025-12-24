package com.lms.model;

import java.sql.Timestamp;

public class Document {

    private int id;
    private int userId;
    private String tenFileGoc;
    private String tenFileLuu;
    private String duongDan;
    private String loaiFile;
    private long kichThuoc;
    private String monHoc;
    private String moTa;
    private Timestamp uploadedAt;

    public Document() {
    }

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

    public String getTenFileGoc() {
        return tenFileGoc;
    }

    public void setTenFileGoc(String tenFileGoc) {
        this.tenFileGoc = tenFileGoc;
    }

    public String getTenFileLuu() {
        return tenFileLuu;
    }

    public void setTenFileLuu(String tenFileLuu) {
        this.tenFileLuu = tenFileLuu;
    }

    public String getDuongDan() {
        return duongDan;
    }

    public void setDuongDan(String duongDan) {
        this.duongDan = duongDan;
    }

    public String getLoaiFile() {
        return loaiFile;
    }

    public void setLoaiFile(String loaiFile) {
        this.loaiFile = loaiFile;
    }

    public long getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(long kichThuoc) {
        this.kichThuoc = kichThuoc;
    }

    public String getMonHoc() {
        return monHoc;
    }

    public void setMonHoc(String monHoc) {
        this.monHoc = monHoc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
