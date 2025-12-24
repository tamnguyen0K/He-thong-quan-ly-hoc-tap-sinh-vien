# Sơ Đồ Luồng Hoạt Động - ScheduleDAO

## Mô tả
Data Access Object cho bảng schedules. Xử lý các thao tác CRUD với database và kiểm tra trùng lịch.

## Sơ Đồ Luồng - Phương Thức findAllByUserId

```mermaid
flowchart TD
    A[Bắt đầu: findAllByUserId] --> B[Chuẩn bị SQL với ORDER BY:<br/>Sắp xếp theo thứ trong tuần<br/>và thời gian bắt đầu]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set userId vào parameter]
    E --> F[Thực thi query]
    F --> G[Khởi tạo List Schedule]
    G --> H[Lặp qua ResultSet]
    H --> I{Còn dòng?}
    I -->|Có| J[Map ResultSet thành Schedule]
    J --> K[Thêm vào List]
    K --> H
    I -->|Không| L[Trả về List]
    L --> M[Kết thúc]
    
    style A fill:#e1f5ff
    style M fill:#c8e6c9
    style I fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức findById

```mermaid
flowchart TD
    A[Bắt đầu: findById] --> B[Chuẩn bị SQL:<br/>SELECT * FROM schedules WHERE id = ?]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set id vào parameter]
    E --> F[Thực thi query]
    F --> G{Có kết quả?}
    G -->|Có| H[Map ResultSet thành Schedule object]
    G -->|Không| I[Trả về null]
    H --> J[Trả về Schedule]
    J --> K[Kết thúc]
    I --> K
    
    style A fill:#e1f5ff
    style K fill:#c8e6c9
    style G fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức create

```mermaid
flowchart TD
    A[Bắt đầu: create] --> B[Chuẩn bị SQL:<br/>INSERT INTO schedules<br/>user_id, ten_mon, thu_trong_tuan,<br/>thoi_gian_bat_dau, thoi_gian_ket_thuc,<br/>phong_hoc, ghi_chu]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement<br/>với RETURN_GENERATED_KEYS]
    D --> E[Set các parameters]
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

## Sơ Đồ Luồng - Phương Thức update

```mermaid
flowchart TD
    A[Bắt đầu: update] --> B[Chuẩn bị SQL:<br/>UPDATE schedules SET<br/>ten_mon, thu_trong_tuan,<br/>thoi_gian_bat_dau, thoi_gian_ket_thuc,<br/>phong_hoc, ghi_chu WHERE id = ?]
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

## Sơ Đồ Luồng - Phương Thức delete

```mermaid
flowchart TD
    A[Bắt đầu: delete] --> B[Chuẩn bị SQL:<br/>DELETE FROM schedules WHERE id = ?]
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

## Sơ Đồ Luồng - Phương Thức checkConflict

```mermaid
flowchart TD
    A[Bắt đầu: checkConflict] --> B[Chuẩn bị SQL:<br/>SELECT COUNT(*) FROM schedules<br/>WHERE user_id = ? AND thu_trong_tuan = ?<br/>AND kiểm tra trùng thời gian]
    B --> C{excludeId > 0?}
    C -->|Có| D[Thêm điều kiện: AND id != ?]
    C -->|Không| E[Lấy Connection từ DBUtil]
    D --> E
    E --> F[Tạo PreparedStatement]
    F --> G[Set các parameters:<br/>userId, thu, batDau, ketThuc, excludeId]
    G --> H[Thực thi query]
    H --> I[Lấy COUNT từ ResultSet]
    I --> J{COUNT > 0?}
    J -->|Có| K[Trả về true - Có trùng]
    J -->|Không| L[Trả về false - Không trùng]
    K --> M[Kết thúc]
    L --> M
    
    style A fill:#e1f5ff
    style M fill:#c8e6c9
    style C fill:#fff9c4
    style J fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Sắp Xếp Lịch Học
- Sắp xếp theo thứ trong tuần (Thứ 2 -> Chủ nhật)
- Sau đó sắp xếp theo thời gian bắt đầu
- Sử dụng CASE statement trong SQL để sắp xếp thứ

### 2. Kiểm Tra Trùng Lịch
- Kiểm tra xem có lịch học nào trùng thời gian không
- Điều kiện trùng:
  - Cùng user_id
  - Cùng thứ trong tuần
  - Thời gian bị chồng chéo (một trong các trường hợp):
    - Lịch mới bắt đầu trong khoảng lịch cũ
    - Lịch mới kết thúc trong khoảng lịch cũ
    - Lịch mới bao trùm lịch cũ hoàn toàn
- Hỗ trợ excludeId để loại trừ chính nó khi update

### 3. Xử Lý Thời Gian
- Sử dụng Time object để lưu thời gian bắt đầu và kết thúc
- So sánh thời gian trong SQL để kiểm tra trùng

### 4. Mapping Dữ Liệu
- Map từ ResultSet sang Schedule object
- Xử lý các trường có thể null (phongHoc, ghiChu)

