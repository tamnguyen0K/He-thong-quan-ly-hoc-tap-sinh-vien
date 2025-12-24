# Sơ Đồ Luồng Hoạt Động - User Model

## Mô tả
Model class đại diện cho bảng users. Lưu trữ thông tin tài khoản người dùng.

## Cấu Trúc Dữ Liệu

```mermaid
classDiagram
    class User {
        -int id
        -String email
        -String passwordHash
        -String role
        -String hoTen
        -Timestamp createdAt
        +User()
        +User(id, email, passwordHash, role, hoTen, createdAt)
        +User(email, passwordHash, role)
        +User(email, passwordHash, role, hoTen)
        +getId() int
        +setId(int id)
        +getEmail() String
        +setEmail(String email)
        +getPasswordHash() String
        +setPasswordHash(String hash)
        +getRole() String
        +setRole(String role)
        +getHoTen() String
        +setHoTen(String hoTen)
        +getCreatedAt() Timestamp
        +setCreatedAt(Timestamp time)
        +toString() String
    }
```

## Sơ Đồ Luồng - Khởi Tạo Object

```mermaid
flowchart TD
    A[Tạo User object] --> B{Constructor nào?}
    B -->|User| C[Tạo object rỗng<br/>Tất cả fields = null/0]
    B -->|User id, email, ...| D[Tạo object với đầy đủ thông tin<br/>bao gồm id và createdAt]
    B -->|User email, passwordHash, role| E[Tạo object khi đăng ký<br/>chưa có id và createdAt]
    B -->|User email, passwordHash, role, hoTen| F[Tạo object khi đăng ký có tên<br/>chưa có id và createdAt]
    C --> G[Object sẵn sàng sử dụng]
    D --> G
    E --> G
    F --> G
    
    style A fill:#e1f5ff
    style G fill:#c8e6c9
    style B fill:#fff9c4
```

## Sơ Đồ Luồng - Getter và Setter

```mermaid
flowchart TD
    A[Sử dụng User object] --> B{Cần làm gì?}
    B -->|Lấy giá trị| C[Sử dụng getter methods:<br/>getId, getEmail, getPasswordHash,<br/>getRole, getHoTen, getCreatedAt]
    B -->|Thiết lập giá trị| D[Sử dụng setter methods:<br/>setId, setEmail, setPasswordHash,<br/>setRole, setHoTen, setCreatedAt]
    C --> E[Trả về giá trị]
    D --> F[Thiết lập giá trị mới]
    E --> G[Hoàn thành]
    F --> G
    
    style A fill:#e1f5ff
    style G fill:#c8e6c9
    style B fill:#fff9c4
```

## Chi Tiết Các Trường

### 1. id (int)
- ID tự động tăng của user
- Được tạo bởi database khi insert

### 2. email (String)
- Email đăng nhập của user
- Phải là duy nhất trong hệ thống

### 3. passwordHash (String)
- Mật khẩu đã được hash bằng BCrypt
- Không bao giờ lưu mật khẩu dạng plain text

### 4. role (String)
- Vai trò của user trong hệ thống
- Mặc định là "student"

### 5. hoTen (String)
- Họ và tên của user
- Có thể null

### 6. createdAt (Timestamp)
- Thời gian tạo tài khoản
- Được set tự động bởi database

## Các Constructor

1. **User()**: Constructor mặc định, tạo object rỗng
2. **User(id, email, passwordHash, role, hoTen, createdAt)**: Constructor đầy đủ, dùng khi đọc từ database
3. **User(email, passwordHash, role)**: Constructor khi tạo user mới, chưa có id và createdAt
4. **User(email, passwordHash, role, hoTen)**: Constructor khi tạo user mới có tên, chưa có id và createdAt

