# Sơ Đồ Luồng Hoạt Động - DocumentDAO

## Mô tả
Data Access Object cho bảng documents. Xử lý các thao tác CRUD với database.

## Sơ Đồ Luồng - Phương Thức findAllByUserId

```mermaid
flowchart TD
    A[Bắt đầu: findAllByUserId] --> B[Chuẩn bị SQL:<br/>SELECT * FROM documents<br/>WHERE user_id = ?<br/>ORDER BY uploaded_at DESC]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set userId vào parameter]
    E --> F[Thực thi query]
    F --> G[Khởi tạo List Document]
    G --> H[Lặp qua ResultSet]
    H --> I{Còn dòng?}
    I -->|Có| J[Map ResultSet thành Document]
    J --> K[Thêm vào List]
    K --> H
    I -->|Không| L[Trả về List]
    L --> M[Kết thúc]
    
    style A fill:#e1f5ff
    style M fill:#c8e6c9
    style I fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức findByUserIdAndMonHoc

```mermaid
flowchart TD
    A[Bắt đầu: findByUserIdAndMonHoc] --> B[Chuẩn bị SQL:<br/>SELECT * FROM documents<br/>WHERE user_id = ? AND mon_hoc = ?<br/>ORDER BY uploaded_at DESC]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set userId và monHoc vào parameters]
    E --> F[Thực thi query]
    F --> G[Khởi tạo List Document]
    G --> H[Lặp qua ResultSet]
    H --> I{Còn dòng?}
    I -->|Có| J[Map ResultSet thành Document]
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
    A[Bắt đầu: create] --> B[Chuẩn bị SQL:<br/>INSERT INTO documents<br/>user_id, ten_file_goc, ten_file_luu,<br/>duong_dan, loai_file, kich_thuoc,<br/>mon_hoc, mo_ta]
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

## Sơ Đồ Luồng - Phương Thức delete

```mermaid
flowchart TD
    A[Bắt đầu: delete] --> B[Chuẩn bị SQL:<br/>DELETE FROM documents WHERE id = ?]
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

## Chi Tiết Các Bước

### 1. Lấy Danh Sách Tài Liệu
- `findAllByUserId`: Lấy tất cả tài liệu của user
- `findByUserIdAndMonHoc`: Lọc theo môn học
- Sắp xếp theo thời gian upload (mới nhất trước)

### 2. Lưu Thông Tin File
- Lưu cả tên file gốc (ten_file_goc) và tên file lưu (ten_file_luu)
- Tên file lưu là UUID để tránh trùng lặp
- Lưu đường dẫn đầy đủ (duong_dan) để truy cập file
- Lưu loại file (loai_file) và kích thước (kich_thuoc)

### 3. Xóa Tài Liệu
- Chỉ xóa bản ghi trong database
- File vật lý phải được xóa riêng ở controller

### 4. Mapping Dữ Liệu
- Map từ ResultSet sang Document object
- Xử lý các trường có thể null (monHoc, moTa)

### 5. Lưu Ý
- DocumentDAO chỉ quản lý metadata của file
- File vật lý được lưu trong thư mục uploads/documents
- Cần đồng bộ giữa database và file system khi xóa

