# Sơ Đồ Luồng Hoạt Động - Grade Model

## Mô tả
Model class đại diện cho bảng grades. Lưu trữ điểm số của sinh viên.

## Cấu Trúc Dữ Liệu

```mermaid
classDiagram
    class Grade {
        -int id
        -int userId
        -String tenMon
        -String namHoc
        -String hocKy
        -int tinChi
        -BigDecimal diemSo
        -String loaiDiem
        -String ghiChu
        -Timestamp createdAt
        -Timestamp updatedAt
        +Grade()
        +getId() int
        +setId(int id)
        +getUserId() int
        +setUserId(int userId)
        +getTenMon() String
        +setTenMon(String tenMon)
        +getNamHoc() String
        +setNamHoc(String namHoc)
        +getHocKy() String
        +setHocKy(String hocKy)
        +getTinChi() int
        +setTinChi(int tinChi)
        +getDiemSo() BigDecimal
        +setDiemSo(BigDecimal diem)
        +getLoaiDiem() String
        +setLoaiDiem(String loai)
        +getGhiChu() String
        +setGhiChu(String ghiChu)
        +getCreatedAt() Timestamp
        +setCreatedAt(Timestamp time)
        +getUpdatedAt() Timestamp
        +setUpdatedAt(Timestamp time)
    }
```

## Sơ Đồ Luồng - Các Loại Điểm

```mermaid
flowchart TD
    A[Grade object] --> B{loaiDiem là gì?}
    B -->|quatrinh| C[Điểm quá trình<br/>Trọng số: 20%]
    B -->|giuaky| D[Điểm giữa kỳ<br/>Trọng số: 30%]
    B -->|ketthuc| E[Điểm kết thúc<br/>Trọng số: 50%]
    B -->|tongket| F[Điểm tổng kết<br/>Tự động tính từ 3 loại trên]
    C --> G[Được nhập thủ công]
    D --> G
    E --> G
    F --> H[Được tính tự động<br/>khi có đủ 3 loại điểm]
    
    style A fill:#e1f5ff
    style G fill:#c8e6c9
    style H fill:#c8e6c9
    style B fill:#fff9c4
```

## Chi Tiết Các Trường

### 1. id (int)
- ID tự động tăng của điểm
- Được tạo bởi database khi insert

### 2. userId (int)
- ID của user sở hữu điểm này
- Foreign key tham chiếu đến users.id

### 3. tenMon (String)
- Tên môn học
- Bắt buộc, không được rỗng

### 4. namHoc (String)
- Năm học (ví dụ: 2023-2024)
- Có thể null

### 5. hocKy (String)
- Học kỳ (ví dụ: Học kỳ 1, Học kỳ 2)
- Bắt buộc, không được rỗng

### 6. tinChi (int)
- Số tín chỉ của môn học
- Bắt buộc, phải > 0

### 7. diemSo (BigDecimal)
- Điểm số (thang điểm 10)
- Bắt buộc, không được null
- Sử dụng BigDecimal để đảm bảo độ chính xác

### 8. loaiDiem (String)
- Loại điểm: quatrinh, giuaky, ketthuc, tongket
- Bắt buộc, không được rỗng

### 9. ghiChu (String)
- Ghi chú về điểm
- Tùy chọn, có thể null

### 10. createdAt (Timestamp)
- Thời gian tạo điểm
- Được set tự động bởi database

### 11. updatedAt (Timestamp)
- Thời gian cập nhật điểm lần cuối
- Được set tự động bởi database khi update

## Tính Điểm Tổng Kết

```mermaid
flowchart TD
    A[Có đủ 3 loại điểm] --> B[Lấy điểm quá trình]
    B --> C[Lấy điểm giữa kỳ]
    C --> D[Lấy điểm kết thúc]
    D --> E[Tính điểm tổng kết:<br/>quatrinh * 0.2 +<br/>giuaky * 0.3 +<br/>ketthuc * 0.5]
    E --> F[Làm tròn 2 chữ số thập phân]
    F --> G[Tạo Grade object mới<br/>với loaiDiem = 'tongket']
    G --> H[Lưu vào database]
    
    style A fill:#e1f5ff
    style H fill:#c8e6c9
```

