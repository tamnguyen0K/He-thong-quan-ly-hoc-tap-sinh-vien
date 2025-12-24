# Sơ Đồ Luồng Hoạt Động - Schedule Model

## Mô tả
Model class đại diện cho bảng schedules. Lưu trữ lịch học của sinh viên.

## Cấu Trúc Dữ Liệu

```mermaid
classDiagram
    class Schedule {
        -int id
        -int userId
        -String tenMon
        -String thuTrongTuan
        -Time thoiGianBatDau
        -Time thoiGianKetThuc
        -String phongHoc
        -String ghiChu
        -Timestamp createdAt
        +Schedule()
        +Schedule(id, userId, tenMon, thuTrongTuan, thoiGianBatDau, thoiGianKetThuc, phongHoc, ghiChu, createdAt)
        +getId() int
        +setId(int id)
        +getUserId() int
        +setUserId(int userId)
        +getTenMon() String
        +setTenMon(String tenMon)
        +getThuTrongTuan() String
        +setThuTrongTuan(String thu)
        +getThoiGianBatDau() Time
        +setThoiGianBatDau(Time time)
        +getThoiGianKetThuc() Time
        +setThoiGianKetThuc(Time time)
        +getPhongHoc() String
        +setPhongHoc(String phong)
        +getGhiChu() String
        +setGhiChu(String ghiChu)
        +getCreatedAt() Timestamp
        +setCreatedAt(Timestamp time)
    }
```

## Sơ Đồ Luồng - Khởi Tạo Object

```mermaid
flowchart TD
    A[Tạo Schedule object] --> B{Constructor nào?}
    B -->|Schedule| C[Tạo object rỗng<br/>Tất cả fields = null/0]
    B -->|Schedule id, userId, ...| D[Tạo object với đầy đủ thông tin<br/>bao gồm id và createdAt]
    C --> E[Object sẵn sàng sử dụng]
    D --> E
    
    style A fill:#e1f5ff
    style E fill:#c8e6c9
    style B fill:#fff9c4
```

## Sơ Đồ Luồng - Validate Dữ Liệu

```mermaid
flowchart TD
    A[Tạo Schedule object] --> B[Set các thông tin]
    B --> C{Validate dữ liệu}
    C -->|tenMon rỗng| D[Lỗi: Tên môn không được rỗng]
    C -->|thuTrongTuan rỗng| E[Lỗi: Thứ trong tuần không được rỗng]
    C -->|thoiGianBatDau null| F[Lỗi: Thời gian bắt đầu không được null]
    C -->|thoiGianKetThuc null| G[Lỗi: Thời gian kết thúc không được null]
    C -->|ketThuc <= batDau| H[Lỗi: Thời gian kết thúc phải sau bắt đầu]
    C -->|Hợp lệ| I[Object hợp lệ]
    D --> J[Kết thúc với lỗi]
    E --> J
    F --> J
    G --> J
    H --> J
    I --> K[Kết thúc thành công]
    
    style A fill:#e1f5ff
    style K fill:#c8e6c9
    style J fill:#ffcdd2
    style C fill:#fff9c4
```

## Chi Tiết Các Trường

### 1. id (int)
- ID tự động tăng của lịch học
- Được tạo bởi database khi insert

### 2. userId (int)
- ID của user sở hữu lịch học này
- Foreign key tham chiếu đến users.id

### 3. tenMon (String)
- Tên môn học
- Bắt buộc, không được rỗng

### 4. thuTrongTuan (String)
- Thứ trong tuần (Thứ 2, Thứ 3, ..., Chủ nhật)
- Bắt buộc, không được rỗng

### 5. thoiGianBatDau (Time)
- Thời gian bắt đầu học
- Bắt buộc, không được null
- Định dạng: HH:mm:ss

### 6. thoiGianKetThuc (Time)
- Thời gian kết thúc học
- Bắt buộc, không được null
- Phải sau thoiGianBatDau
- Định dạng: HH:mm:ss

### 7. phongHoc (String)
- Phòng học
- Tùy chọn, có thể null

### 8. ghiChu (String)
- Ghi chú về lịch học
- Tùy chọn, có thể null

### 9. createdAt (Timestamp)
- Thời gian tạo lịch học
- Được set tự động bởi database

