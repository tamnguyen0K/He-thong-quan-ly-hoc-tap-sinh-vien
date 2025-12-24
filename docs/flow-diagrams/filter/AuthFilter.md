# Sơ Đồ Luồng Hoạt Động - AuthFilter

## Mô tả
Filter để bảo vệ các trang cần đăng nhập. Cho phép truy cập /login, /register, /logout và các file tĩnh. Các trang khác yêu cầu đăng nhập.

## Sơ Đồ Luồng - Phương Thức doFilter

```mermaid
flowchart TD
    A[Bắt đầu: doFilter] --> B[Cast request/response<br/>sang HttpServletRequest/Response]
    B --> C[Lấy request URI và context path]
    C --> D[Tính relative path<br/>path - contextPath]
    D --> E{isPublicPath relativePath?}
    E -->|Có| F[Cho phép truy cập<br/>chain.doFilter]
    E -->|Không| G[Lấy session hiện tại<br/>không tạo mới]
    G --> H{Session tồn tại?}
    H -->|Không| I[Lưu URL hiện tại vào redirect parameter]
    I --> J[Redirect đến /login<br/>với redirect parameter]
    H -->|Có| K{Lấy user từ session}
    K --> L{User tồn tại?}
    L -->|Không| I
    L -->|Có| M[Cho phép truy cập<br/>chain.doFilter]
    F --> N[Kết thúc]
    J --> N
    M --> N
    
    style A fill:#e1f5ff
    style N fill:#c8e6c9
    style E fill:#fff9c4
    style H fill:#fff9c4
    style L fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức isPublicPath

```mermaid
flowchart TD
    A[Bắt đầu: isPublicPath] --> B[Nhận path]
    B --> C{path == '/login'?}
    C -->|Có| D[Trả về true]
    C -->|Không| E{path == '/register'?}
    E -->|Có| D
    E -->|Không| F{path == '/logout'?}
    F -->|Có| D
    F -->|Không| G{path bắt đầu với<br/>'/assets/' hoặc '/css/'<br/>hoặc '/js/' hoặc '/images/'<br/>hoặc '/uploads/'?}
    G -->|Có| D
    G -->|Không| H{path == '/' hoặc<br/>path == '/home'?}
    H -->|Có| D
    H -->|Không| I{path == '/test-dao'?}
    I -->|Có| D
    I -->|Không| J[Trả về false]
    D --> K[Kết thúc]
    J --> K
    
    style A fill:#e1f5ff
    style K fill:#c8e6c9
    style C fill:#fff9c4
    style E fill:#fff9c4
    style F fill:#fff9c4
    style G fill:#fff9c4
    style H fill:#fff9c4
    style I fill:#fff9c4
```

## Sơ Đồ Luồng - Xử Lý Redirect Sau Đăng Nhập

```mermaid
flowchart TD
    A[User truy cập trang được bảo vệ] --> B[AuthFilter chặn]
    B --> C[Lưu URL hiện tại vào redirect parameter]
    C --> D[Redirect đến /login?redirect=URL]
    D --> E[User đăng nhập thành công]
    E --> F[LoginServlet kiểm tra redirect parameter]
    F --> G{Có redirect parameter?}
    G -->|Có| H[Redirect đến URL đã lưu]
    G -->|Không| I[Redirect đến /dashboard]
    H --> J[User truy cập trang ban đầu]
    I --> K[User vào dashboard]
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
    style K fill:#c8e6c9
    style G fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Kiểm Tra Public Path
- Các path không cần đăng nhập:
  - /login, /register, /logout
  - /, /home
  - /assets/, /css/, /js/, /images/, /uploads/
  - /test-dao (có thể xóa sau)

### 2. Kiểm Tra Session
- Lấy session hiện tại mà không tạo mới (getSession(false))
- Nếu không có session, redirect đến login

### 3. Kiểm Tra User
- Lấy user từ session attribute "user"
- Nếu không có user, redirect đến login

### 4. Lưu Redirect URL
- Khi redirect đến login, lưu URL hiện tại vào parameter "redirect"
- LoginServlet sẽ redirect lại sau khi đăng nhập thành công

### 5. Cho Phép Truy Cập
- Nếu đã đăng nhập, gọi chain.doFilter() để tiếp tục
- Request được chuyển đến servlet hoặc resource tiếp theo

### 6. WebFilter Annotation
- @WebFilter("/*") áp dụng cho tất cả requests
- Filter được thực thi trước servlet

