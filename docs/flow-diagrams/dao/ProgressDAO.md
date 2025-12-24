# Sơ Đồ Luồng Hoạt Động - ProgressDAO

## Mô tả
Data Access Object cho bảng progress. Xử lý các thao tác CRUD với database và toggle trạng thái.

## Sơ Đồ Luồng - Phương Thức findAllByUserId

```mermaid
flowchart TD
    A[Bắt đầu: findAllByUserId] --> B[Chuẩn bị SQL:<br/>SELECT * FROM progress<br/>WHERE user_id = ?<br/>ORDER BY han_nop ASC]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set userId vào parameter]
    E --> F[Thực thi query]
    F --> G[Khởi tạo List Progress]
    G --> H[Lặp qua ResultSet]
    H --> I{Còn dòng?}
    I -->|Có| J[Map ResultSet thành Progress]
    J --> K[Thêm vào List]
    K --> H
    I -->|Không| L[Trả về List]
    L --> M[Kết thúc]
    
    style A fill:#e1f5ff
    style M fill:#c8e6c9
    style I fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức create

```mermaid
flowchart TD
    A[Bắt đầu: create] --> B[Chuẩn bị SQL:<br/>INSERT INTO progress<br/>user_id, ten_task, loai_task,<br/>mon_hoc, trang_thai, han_nop, mo_ta]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement<br/>với RETURN_GENERATED_KEYS]
    D --> E[Set các parameters:<br/>Nếu trang_thai null thì mặc định 'chuaxong']
    E --> F[Thực thi executeUpdate]
    F --> G{affectedRows > 0?}
    G -->|Không| H[Trả về -1]
    G -->|Có| I[Lấy GeneratedKeys]
    I --> J{Có key?}
    J -->|Có| K[Lấy ID vừa tạo]
    J -->|Không| H
    K --> L[Trả về ID]
    L --> M[Kết thúc]
    H --> M
    
    style A fill:#e1f5ff
    style M fill:#c8e6c9
    style G fill:#fff9c4
    style J fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức toggleStatus

```mermaid
flowchart TD
    A[Bắt đầu: toggleStatus] --> B[Chuẩn bị SQL:<br/>UPDATE progress SET<br/>trang_thai = CASE<br/>WHEN trang_thai='chuaxong'<br/>THEN 'daxong'<br/>ELSE 'chuaxong' END,<br/>completed_at = CASE<br/>WHEN trang_thai='chuaxong'<br/>THEN NOW()<br/>ELSE NULL END<br/>WHERE id = ?]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set id vào parameter]
    E --> F[Thực thi executeUpdate]
    F --> G{affectedRows > 0?}
    G -->|Có| H[Trả về true]
    G -->|Không| I[Trả về false]
    H --> J[Kết thúc]
    I --> J
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
    style G fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức update

```mermaid
flowchart TD
    A[Bắt đầu: update] --> B[Chuẩn bị SQL:<br/>UPDATE progress SET<br/>ten_task, loai_task, mon_hoc,<br/>han_nop, mo_ta WHERE id = ?]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set các parameters]
    E --> F[Thực thi executeUpdate]
    F --> G{affectedRows > 0?}
    G -->|Có| H[Trả về true]
    G -->|Không| I[Trả về false]
    H --> J[Kết thúc]
    I --> J
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
    style G fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Sắp Xếp Tasks
- Sắp xếp theo hạn nộp (han_nop) tăng dần
- Giúp hiển thị các task sắp đến hạn trước

### 2. Trạng Thái Mặc Định
- Khi tạo task mới, trạng thái mặc định là "chuaxong"
- Có thể set trạng thái khác nếu cần

### 3. Toggle Trạng Thái
- Chuyển đổi giữa "chuaxong" và "daxong"
- Khi chuyển sang "daxong", tự động set completed_at = NOW()
- Khi chuyển về "chuaxong", set completed_at = NULL
- Sử dụng CASE statement trong SQL để thực hiện trong một query

### 4. Cập Nhật Task
- Cập nhật các trường: tenTask, loaiTask, monHoc, hanNop, moTa
- Không cập nhật trang_thai (phải dùng toggleStatus)

### 5. Mapping Dữ Liệu
- Map từ ResultSet sang Progress object
- Xử lý các trường có thể null (hanNop, moTa, completedAt)

