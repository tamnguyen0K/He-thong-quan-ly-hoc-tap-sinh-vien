# Sơ Đồ Luồng Hoạt Động - HomeServlet

## Mô tả
Servlet xử lý trang chủ của ứng dụng. Hiển thị trang home.jsp, có thể xem được cả khi chưa đăng nhập.

## Sơ Đồ Luồng - Phương Thức doGet

```mermaid
flowchart TD
    A[Bắt đầu: doGet] --> B[Lấy Request và Response]
    B --> C[Lấy session hiện tại<br/>không tạo mới nếu chưa có]
    C --> D{Session tồn tại?}
    D -->|Có| E{Lấy user từ session}
    D -->|Không| F[Forward đến home.jsp]
    E --> G{User tồn tại?}
    G -->|Có| H[Set user vào request attribute]
    G -->|Không| F
    H --> F
    F --> I[Hiển thị trang chủ]
    I --> J[Kết thúc]
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
    style D fill:#fff9c4
    style G fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức doPost

```mermaid
flowchart TD
    A[Bắt đầu: doPost] --> B[Gọi doGet]
    B --> C[Kết thúc]
    
    style A fill:#e1f5ff
    style C fill:#c8e6c9
```

## Chi Tiết Các Bước

### 1. Kiểm Tra Session
- Lấy session hiện tại mà không tạo mới nếu chưa có
- Điều này cho phép trang chủ hiển thị được cả khi chưa đăng nhập

### 2. Lấy Thông Tin User
- Nếu có session và có user trong session, lấy user ra
- Set user vào request attribute để hiển thị trong JSP

### 3. Hiển Thị Trang Chủ
- Forward request đến `/WEB-INF/views/home.jsp`
- Trang chủ có thể hiển thị thông tin user nếu đã đăng nhập, hoặc hiển thị thông tin chung nếu chưa đăng nhập

### 4. URL Patterns
- Servlet được map với các URL: `/` và `/home`
- Cả hai đều trỏ đến cùng một trang chủ

