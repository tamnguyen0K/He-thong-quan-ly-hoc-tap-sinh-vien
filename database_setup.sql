-- Tạo database quan_ly_hoc_tap
CREATE DATABASE IF NOT EXISTS quan_ly_hoc_tap
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE quan_ly_hoc_tap;

-- Bảng users
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'student',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng user_profiles
CREATE TABLE user_profiles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    ho_ten VARCHAR(255),
    chuyen_nganh VARCHAR(255),
    nam_hoc VARCHAR(50),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_profile (user_id)
);

-- Bảng schedules
CREATE TABLE schedules (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    ten_mon VARCHAR(255) NOT NULL,
    thu_trong_tuan VARCHAR(20) NOT NULL,
    thoi_gian_bat_dau TIME NOT NULL,
    thoi_gian_ket_thuc TIME NOT NULL,
    phong_hoc VARCHAR(100),
    ghi_chu TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng progress
CREATE TABLE progress (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    ten_task VARCHAR(255) NOT NULL,
    loai_task VARCHAR(50),
    mon_hoc VARCHAR(255),
    trang_thai VARCHAR(50) DEFAULT 'chuaxong',
    han_nop DATETIME,
    mo_ta TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng grades
CREATE TABLE grades (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    ten_mon VARCHAR(255) NOT NULL,
    hoc_ky VARCHAR(100) NOT NULL,
    tin_chi INT NOT NULL,
    diem_so DECIMAL(4,2) NOT NULL,
    loai_diem VARCHAR(50),
    ghi_chu TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng gpa
CREATE TABLE gpa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    hoc_ky VARCHAR(100),
    gpa_value DECIMAL(4,2),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_term (user_id, hoc_ky)
);

-- Bảng documents
CREATE TABLE documents (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    ten_file_goc VARCHAR(255) NOT NULL,
    ten_file_luu VARCHAR(255) NOT NULL,
    duong_dan VARCHAR(500) NOT NULL,
    loai_file VARCHAR(50),
    kich_thuoc BIGINT,
    mon_hoc VARCHAR(255),
    mo_ta TEXT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

