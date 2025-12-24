# Sơ Đồ Luồng Hoạt Động - Progress Model

## Mô tả
Model class đại diện cho bảng progress. Lưu trữ tiến độ học tập (các task) của sinh viên.

## Cấu Trúc Dữ Liệu

```mermaid
classDiagram
    class Progress {
        -int id
        -int userId
        -String tenTask
        -String loaiTask
        -String monHoc
        -String trangThai
        -Timestamp hanNop
        -String moTa
        -Timestamp createdAt
        -Timestamp completedAt
        +Progress()
        +getId() int
        +setId(int id)
        +getUserId() int
        +setUserId(int userId)
        +getTenTask() String
        +setTenTask(String tenTask)
        +getLoaiTask() String
        +setLoaiTask(String loai)
        +getMonHoc() String
        +setMonHoc(String monHoc)
        +getTrangThai() String
        +setTrangThai(String trangThai)
        +getHanNop() Timestamp
        +setHanNop(Timestamp hanNop)
        +getMoTa() String
        +setMoTa(String moTa)
        +getCreatedAt() Timestamp
        +setCreatedAt(Timestamp time)
        +getCompletedAt() Timestamp
        +setCompletedAt(Timestamp time)
    }
```

## Sơ Đồ Luồng - Trạng Thái Task

```mermaid
flowchart TD
    A[Progress object] --> B{trangThai là gì?}
    B -->|chuaxong| C[Task chưa hoàn thành<br/>completedAt = null]
    B -->|daxong| D[Task đã hoàn thành<br/>completedAt = thời gian hoàn thành]
    C --> E[Có thể toggle sang 'daxong']
    D --> F[Có thể toggle sang 'chuaxong']
    E --> G[Khi toggle sang 'daxong'<br/>set completedAt = NOW]
    F --> H[Khi toggle sang 'chuaxong'<br/>set completedAt = null]
    
    style A fill:#e1f5ff
    style G fill:#c8e6c9
    style H fill:#c8e6c9
    style B fill:#fff9c4
```

## Chi Tiết Các Trường

### 1. id (int)
- ID tự động tăng của task
- Được tạo bởi database khi insert

### 2. userId (int)
- ID của user sở hữu task này
- Foreign key tham chiếu đến users.id

### 3. tenTask (String)
- Tên của task
- Bắt buộc, không được rỗng

### 4. loaiTask (String)
- Loại task (ví dụ: Bài tập, Đồ án, Báo cáo)
- Có thể null

### 5. monHoc (String)
- Môn học liên quan đến task
- Có thể null

### 6. trangThai (String)
- Trạng thái task: "chuaxong" hoặc "daxong"
- Mặc định là "chuaxong" khi tạo mới

### 7. hanNop (Timestamp)
- Hạn nộp của task
- Tùy chọn, có thể null
- Định dạng: yyyy-MM-dd HH:mm:ss

### 8. moTa (String)
- Mô tả chi tiết về task
- Tùy chọn, có thể null

### 9. createdAt (Timestamp)
- Thời gian tạo task
- Được set tự động bởi database

### 10. completedAt (Timestamp)
- Thời gian hoàn thành task
- Được set tự động khi trangThai chuyển sang "daxong"
- Null khi trangThai là "chuaxong"

## Sơ Đồ Luồng - Toggle Trạng Thái

```mermaid
flowchart TD
    A[Toggle trạng thái task] --> B{trangThai hiện tại?}
    B -->|chuaxong| C[Chuyển sang 'daxong']
    B -->|daxong| D[Chuyển sang 'chuaxong']
    C --> E[Set completedAt = NOW]
    D --> F[Set completedAt = NULL]
    E --> G[Cập nhật database]
    F --> G
    G --> H[Hoàn thành]
    
    style A fill:#e1f5ff
    style H fill:#c8e6c9
    style B fill:#fff9c4
```

