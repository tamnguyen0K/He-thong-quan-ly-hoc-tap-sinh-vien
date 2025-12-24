# Sơ Đồ Luồng Hoạt Động - LoginServlet

## Mô tả
Servlet xử lý đăng nhập người dùng. Hỗ trợ GET để hiển thị form đăng nhập và POST để xử lý đăng nhập.

## Sơ Đồ Luồng - Phương Thức doGet

```mermaid
flowchart TD
    A[Bắt đầu: doGet] --> B[Lấy parameter 'success' từ request]
    B --> C{Có thông báo thành công?}
    C -->|Có| D[Set success vào request attribute]
    C -->|Không| E[Forward đến login.jsp]
    D --> E
    E --> F[Hiển thị form đăng nhập]
    F --> G[Kết thúc]
    
    style A fill:#e1f5ff
    style G fill:#c8e6c9
```

## Sơ Đồ Luồng - Phương Thức doPost

```mermaid
flowchart TD
    A[Bắt đầu: doPost] --> B[Lấy dữ liệu từ form:<br/>email, password]
    B --> C{Validate dữ liệu}
    C -->|Email rỗng| D1[Thiết lập lỗi: Email không được để trống]
    C -->|Password rỗng| D2[Thiết lập lỗi: Mật khẩu không được để trống]
    C -->|Hợp lệ| E[Tìm user theo email qua UserDAO]
    
    D1 --> F{Có lỗi?}
    D2 --> F
    
    F -->|Có| G[Set error vào request]
    G --> H[Forward lại login.jsp với lỗi]
    H --> I[Kết thúc]
    
    E --> J{User tồn tại?}
    J -->|Không| K[Thiết lập lỗi: Email hoặc mật khẩu không đúng]
    K --> F
    
    J -->|Có| L[Kiểm tra password bằng PasswordUtil.checkPassword]
    L --> M{Password hợp lệ?}
    M -->|Không| K
    
    M -->|Có| N[Tạo session mới]
    N --> O[Lưu user vào session]
    O --> P{Lấy redirect URL từ parameter?}
    P -->|Có| Q[Redirect đến URL đã lưu]
    P -->|Không| R[Redirect đến /dashboard]
    Q --> I
    R --> I
    
    style A fill:#e1f5ff
    style I fill:#c8e6c9
    style F fill:#fff9c4
    style J fill:#fff9c4
    style M fill:#fff9c4
    style P fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Validate Dữ Liệu
- Kiểm tra email không được rỗng
- Kiểm tra mật khẩu không được rỗng

### 2. Xác Thực Người Dùng
- Tìm user theo email sử dụng `UserDAO.findByEmail()`
- Kiểm tra password bằng `PasswordUtil.checkPassword()` với BCrypt

### 3. Tạo Session
- Tạo HttpSession mới
- Lưu đối tượng User vào session để sử dụng cho các request sau

### 4. Redirect
- Nếu có parameter `redirect`, chuyển hướng đến URL đó
- Nếu không, chuyển hướng đến trang dashboard

### 5. Xử Lý Lỗi
- Nếu email hoặc password không đúng, hiển thị thông báo lỗi chung để bảo mật
- Forward lại form đăng nhập với thông báo lỗi

