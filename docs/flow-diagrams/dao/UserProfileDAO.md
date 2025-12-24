# Sơ Đồ Luồng Hoạt Động - UserProfileDAO

## Mô tả
Data Access Object cho bảng user_profiles. Xử lý các thao tác CRUD với database.

## Sơ Đồ Luồng - Phương Thức findByUserId

```mermaid
flowchart TD
    A[Bắt đầu: findByUserId] --> B[Chuẩn bị SQL:<br/>SELECT * FROM user_profiles<br/>WHERE user_id = ?]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set userId vào parameter]
    E --> F[Thực thi query]
    F --> G{Có kết quả?}
    G -->|Có| H[Map ResultSet thành UserProfile object]
    G -->|Không| I[Trả về null]
    H --> J[Trả về UserProfile]
    J --> K[Kết thúc]
    I --> K
    
    style A fill:#e1f5ff
    style K fill:#c8e6c9
    style G fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức create

```mermaid
flowchart TD
    A[Bắt đầu: create] --> B[Chuẩn bị SQL:<br/>INSERT INTO user_profiles<br/>user_id, ho_ten, chuyen_nganh, nam_hoc]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement<br/>với RETURN_GENERATED_KEYS]
    D --> E[Set các parameters:<br/>userId, hoTen, chuyenNganh, namHoc]
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
    A[Bắt đầu: update] --> B[Chuẩn bị SQL:<br/>UPDATE user_profiles SET<br/>ho_ten = ?, chuyen_nganh = ?,<br/>nam_hoc = ? WHERE user_id = ?]
    B --> C[Lấy Connection từ DBUtil]
    C --> D[Tạo PreparedStatement]
    D --> E[Set các parameters:<br/>hoTen, chuyenNganh, namHoc, userId]
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

## Sơ Đồ Luồng - Phương Thức createOrUpdate

```mermaid
flowchart TD
    A[Bắt đầu: createOrUpdate] --> B[Tìm profile hiện tại<br/>qua findByUserId]
    B --> C{Profile tồn tại?}
    C -->|Không| D[Gọi create]
    C -->|Có| E[Set id từ profile hiện tại<br/>vào profile mới]
    E --> F[Gọi update]
    D --> G{Thành công?}
    F --> G
    G -->|Có| H[Trả về true]
    G -->|Không| I[Trả về false]
    H --> J[Kết thúc]
    I --> J
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
    style C fill:#fff9c4
    style G fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức mapResultSetToUserProfile

```mermaid
flowchart TD
    A[Bắt đầu: mapResultSetToUserProfile] --> B[Tạo UserProfile object mới]
    B --> C[Set id từ rs.getInt 'id']
    C --> D[Set userId từ rs.getInt 'user_id']
    D --> E[Set hoTen từ rs.getString 'ho_ten']
    E --> F[Set chuyenNganh từ rs.getString 'chuyen_nganh']
    F --> G[Set namHoc từ rs.getString 'nam_hoc']
    G --> H[Set updatedAt từ rs.getTimestamp 'updated_at']
    H --> I[Trả về UserProfile object]
    I --> J[Kết thúc]
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
```

## Chi Tiết Các Bước

### 1. Tìm Profile Theo User ID
- Mỗi user chỉ có một profile duy nhất
- Sử dụng `user_id` làm khóa tìm kiếm

### 2. Tạo Mới Profile
- Insert vào database với đầy đủ thông tin
- Trả về ID vừa tạo để sử dụng sau này

### 3. Cập Nhật Profile
- Update các trường: hoTen, chuyenNganh, namHoc
- Sử dụng `user_id` làm điều kiện WHERE

### 4. Create Or Update
- Kiểm tra profile đã tồn tại chưa
- Nếu chưa có thì tạo mới, nếu có rồi thì cập nhật
- Đảm bảo mỗi user chỉ có một profile

### 5. Mapping Dữ Liệu
- Map từ ResultSet sang UserProfile object
- Xử lý các trường có thể null (chuyenNganh, namHoc)

