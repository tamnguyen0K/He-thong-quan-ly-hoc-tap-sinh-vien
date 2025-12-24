# Sơ Đồ Luồng Hoạt Động - PasswordUtil

## Mô tả
Utility class để xử lý mã hóa và kiểm tra mật khẩu. Sử dụng BCrypt để hash password.

## Sơ Đồ Luồng - Phương Thức hashPassword

```mermaid
flowchart TD
    A[Bắt đầu: hashPassword] --> B[Nhận password dạng plain text]
    B --> C[BCrypt tự động tạo salt ngẫu nhiên]
    C --> D[Hash password với salt<br/>BCrypt.hashpw password, BCrypt.gensalt]
    D --> E[Trả về password hash]
    E --> F[Kết thúc]
    
    style A fill:#e1f5ff
    style F fill:#c8e6c9
```

## Sơ Đồ Luồng - Phương Thức checkPassword

```mermaid
flowchart TD
    A[Bắt đầu: checkPassword] --> B[Nhận password plain text và hash]
    B --> C[Gọi BCrypt.checkpw password, hash]
    C --> D{Có lỗi exception?}
    D -->|Có| E[In lỗi ra console]
    E --> F[Trả về false]
    D -->|Không| G{Password khớp?}
    G -->|Có| H[Trả về true]
    G -->|Không| I[Trả về false]
    F --> J[Kết thúc]
    H --> J
    I --> J
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
    style D fill:#fff9c4
    style G fill:#fff9c4
```

## Sơ Đồ Luồng - Quy Trình Hash và Verify

```mermaid
flowchart TD
    A[Đăng ký/Đổi mật khẩu] --> B[Nhận password từ user]
    B --> C[Gọi PasswordUtil.hashPassword]
    C --> D[BCrypt tạo salt ngẫu nhiên]
    D --> E[Hash password với salt]
    E --> F[Lưu hash vào database]
    F --> G[Đăng nhập]
    G --> H[Nhận password từ user]
    H --> I[Lấy hash từ database]
    I --> J[Gọi PasswordUtil.checkPassword]
    J --> K[BCrypt extract salt từ hash]
    K --> L[Hash password với salt đó]
    L --> M{Hash khớp với hash trong DB?}
    M -->|Có| N[Đăng nhập thành công]
    M -->|Không| O[Đăng nhập thất bại]
    
    style A fill:#e1f5ff
    style N fill:#c8e6c9
    style O fill:#ffcdd2
    style M fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Hash Password
- Sử dụng BCrypt để hash password
- BCrypt tự động tạo salt ngẫu nhiên cho mỗi password
- Salt được embed trong hash string
- Mỗi lần hash cùng một password sẽ cho kết quả khác nhau (do salt khác nhau)

### 2. Verify Password
- BCrypt tự động extract salt từ hash
- Hash password input với salt đó
- So sánh với hash trong database
- Trả về true nếu khớp, false nếu không khớp

### 3. Bảo Mật
- Không bao giờ lưu password dạng plain text
- BCrypt là one-way hash, không thể reverse
- Salt ngẫu nhiên ngăn chặn rainbow table attacks
- Work factor mặc định của BCrypt là 10 (có thể tùy chỉnh)

### 4. Xử Lý Lỗi
- Bắt exception khi check password
- Trả về false nếu có lỗi
- Log lỗi ra console để debug

### 5. Lưu Ý
- BCrypt hash có độ dài cố định (60 ký tự)
- Format: $2a$10$... (version, cost, salt+hash)
- Cost factor 10 nghĩa là 2^10 = 1024 iterations

