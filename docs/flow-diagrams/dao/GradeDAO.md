# Sơ Đồ Luồng Hoạt Động - GradeDAO

## Mô tả
Data Access Object cho bảng grades. Xử lý các thao tác CRUD với database và các truy vấn đặc biệt.

## Sơ Đồ Luồng - Phương Thức findAllByUserId

```mermaid
flowchart TD
    A[Bắt đầu: findAllByUserId] --> B[Chuẩn bị SQL:<br/>SELECT * FROM grades<br/>WHERE user_id = ?<br/>ORDER BY hoc_ky, ten_mon]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set userId vào parameter]
    E --> F[Thực thi query]
    F --> G[Khởi tạo List Grade]
    G --> H[Lặp qua ResultSet]
    H --> I{Còn dòng?}
    I -->|Có| J[Map ResultSet thành Grade]
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
    A[Bắt đầu: create] --> B[Chuẩn bị SQL:<br/>INSERT INTO grades<br/>user_id, ten_mon, nam_hoc, hoc_ky,<br/>tin_chi, diem_so, loai_diem, ghi_chu]
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

## Sơ Đồ Luồng - Phương Thức hasAllThreeGradeTypes

```mermaid
flowchart TD
    A[Bắt đầu: hasAllThreeGradeTypes] --> B[Chuẩn bị SQL:<br/>SELECT COUNT DISTINCT loai_diem<br/>FROM grades<br/>WHERE user_id = ? AND ten_mon = ?<br/>AND nam_hoc = ? AND hoc_ky = ?<br/>AND loai_diem IN quatrinh, giuaky, ketthuc]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set các parameters:<br/>userId, tenMon, namHoc, hocKy]
    E --> F[Thực thi query]
    F --> G[Lấy COUNT từ ResultSet]
    G --> H{COUNT >= 3?}
    H -->|Có| I[Trả về true]
    H -->|Không| J[Trả về false]
    I --> K[Kết thúc]
    J --> K
    
    style A fill:#e1f5ff
    style K fill:#c8e6c9
    style H fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức findByUserAndSubjectAndType

```mermaid
flowchart TD
    A[Bắt đầu: findByUserAndSubjectAndType] --> B[Chuẩn bị SQL:<br/>SELECT * FROM grades<br/>WHERE user_id = ? AND ten_mon = ?<br/>AND nam_hoc = ? AND hoc_ky = ?<br/>AND loai_diem = ?]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set các parameters]
    E --> F[Thực thi query]
    F --> G{Có kết quả?}
    G -->|Có| H[Map ResultSet thành Grade object]
    G -->|Không| I[Trả về null]
    H --> J[Trả về Grade]
    J --> K[Kết thúc]
    I --> K
    
    style A fill:#e1f5ff
    style K fill:#c8e6c9
    style G fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức findByUserAndSubject

```mermaid
flowchart TD
    A[Bắt đầu: findByUserAndSubject] --> B[Chuẩn bị SQL:<br/>SELECT * FROM grades<br/>WHERE user_id = ? AND ten_mon = ?<br/>AND nam_hoc = ? AND hoc_ky = ?<br/>ORDER BY FIELD loai_diem<br/>quatrinh, giuaky, ketthuc]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set các parameters]
    E --> F[Thực thi query]
    F --> G[Khởi tạo List Grade]
    G --> H[Lặp qua ResultSet]
    H --> I{Còn dòng?}
    I -->|Có| J[Map ResultSet thành Grade]
    J --> K[Thêm vào List]
    K --> H
    I -->|Không| L[Trả về List]
    L --> M[Kết thúc]
    
    style A fill:#e1f5ff
    style M fill:#c8e6c9
    style I fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Quản Lý Điểm Số
- Lưu trữ nhiều loại điểm cho mỗi môn học:
  - quatrinh: Điểm quá trình (20%)
  - giuaky: Điểm giữa kỳ (30%)
  - ketthuc: Điểm kết thúc (50%)
  - tongket: Điểm tổng kết (tự động tính)

### 2. Kiểm Tra Đủ 3 Loại Điểm
- Sử dụng `hasAllThreeGradeTypes` để kiểm tra môn học đã có đủ 3 loại điểm chưa
- Điều kiện: cùng user_id, ten_mon, nam_hoc, hoc_ky
- Phải có cả 3 loại: quatrinh, giuaky, ketthuc

### 3. Tìm Điểm Theo Loại
- `findByUserAndSubjectAndType`: Tìm điểm cụ thể theo loại
- Sử dụng để lấy từng loại điểm khi tính điểm tổng kết

### 4. Lấy Tất Cả Điểm Của Môn Học
- `findByUserAndSubject`: Lấy tất cả điểm của một môn học
- Sắp xếp theo thứ tự: quatrinh, giuaky, ketthuc
- Sử dụng FIELD() trong SQL để sắp xếp theo thứ tự tùy chỉnh

### 5. Xử Lý NULL
- Hỗ trợ nam_hoc có thể NULL
- Sử dụng điều kiện: `(nam_hoc = ? OR (nam_hoc IS NULL AND ? IS NULL))`

