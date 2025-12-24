# Sơ Đồ Luồng Hoạt Động - UserDAO

## Mô tả
Data Access Object cho bảng users. Xử lý các thao tác CRUD với database.

## Sơ Đồ Luồng - Phương Thức findByEmail

```mermaid
flowchart TD
    A[Bắt đầu: findByEmail] --> B[Chuẩn bị SQL:<br/>SELECT * FROM users WHERE email = ?]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set email vào parameter]
    E --> F[Thực thi query]
    F --> G{Có kết quả?}
    G -->|Có| H[Map ResultSet thành User object]
    G -->|Không| I[Trả về null]
    H --> J[Trả về User]
    J --> K[Kết thúc]
    I --> K
    
    style A fill:#e1f5ff
    style K fill:#c8e6c9
    style G fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức create

```mermaid
flowchart TD
    A[Bắt đầu: create] --> B[Chuẩn bị SQL:<br/>INSERT INTO users<br/>email, password_hash, role, ho_ten]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement<br/>với RETURN_GENERATED_KEYS]
    D --> E[Set các parameters:<br/>email, passwordHash, role, hoTen]
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

## Sơ Đồ Luồng - Phương Thức findById

```mermaid
flowchart TD
    A[Bắt đầu: findById] --> B[Chuẩn bị SQL:<br/>SELECT * FROM users WHERE id = ?]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set id vào parameter]
    E --> F[Thực thi query]
    F --> G{Có kết quả?}
    G -->|Có| H[Map ResultSet thành User object]
    G -->|Không| I[Trả về null]
    H --> J[Trả về User]
    J --> K[Kết thúc]
    I --> K
    
    style A fill:#e1f5ff
    style K fill:#c8e6c9
    style G fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức updateHoTen

```mermaid
flowchart TD
    A[Bắt đầu: updateHoTen] --> B[Chuẩn bị SQL:<br/>UPDATE users SET ho_ten = ?<br/>WHERE id = ?]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set hoTen và userId vào parameters]
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

## Sơ Đồ Luồng - Phương Thức mapResultSetToUser

```mermaid
flowchart TD
    A[Bắt đầu: mapResultSetToUser] --> B[Tạo User object mới]
    B --> C[Set id từ rs.getInt 'id']
    C --> D[Set email từ rs.getString 'email']
    D --> E[Set passwordHash từ rs.getString 'password_hash']
    E --> F[Set role từ rs.getString 'role']
    F --> G[Set hoTen từ rs.getString 'ho_ten']
    G --> H[Set createdAt từ rs.getTimestamp 'created_at']
    H --> I[Trả về User object]
    I --> J[Kết thúc]
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
```

## Chi Tiết Các Bước

### 1. Kết Nối Database
- Sử dụng `DBUtil.getConnection()` để lấy connection
- Tự động đóng connection và statement khi kết thúc (try-with-resources)

### 2. PreparedStatement
- Sử dụng PreparedStatement để tránh SQL injection
- Set parameters an toàn trước khi thực thi

### 3. Xử Lý Kết Quả
- Sử dụng ResultSet để đọc dữ liệu từ database
- Map ResultSet thành User object qua phương thức `mapResultSetToUser`

### 4. Xử Lý Lỗi
- Bắt SQLException và in ra console
- Trả về giá trị mặc định (null, -1, false) khi có lỗi

### 5. Generated Keys
- Khi tạo user mới, sử dụng `RETURN_GENERATED_KEYS` để lấy ID tự động tạo
- Trả về ID vừa tạo để sử dụng cho các thao tác tiếp theo

