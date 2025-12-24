# Sơ Đồ Luồng Hoạt Động - ProfileServlet

## Mô tả
Servlet xử lý hồ sơ cá nhân của người dùng. Hỗ trợ GET để hiển thị form và POST để cập nhật thông tin.

## Sơ Đồ Luồng - Phương Thức doGet

```mermaid
flowchart TD
    A[Bắt đầu: doGet] --> B[Set encoding UTF-8 cho response]
    B --> C[Lấy session và user]
    C --> D{User tồn tại?}
    D -->|Không| E[Redirect đến /login]
    D -->|Có| F[Lấy UserProfile từ database<br/>qua UserProfileDAO]
    F --> G{Profile tồn tại?}
    G -->|Không| H[Tạo profile rỗng mới]
    G -->|Có| I[Lấy message từ session<br/>successMessage, errorMessage]
    H --> I
    I --> J[Xóa message khỏi session<br/>sau khi lấy]
    J --> K[Set profile, user, messages<br/>vào request attribute]
    K --> L[Forward đến profile.jsp]
    L --> M[Hiển thị form hồ sơ]
    M --> N[Kết thúc]
    E --> N
    
    style A fill:#e1f5ff
    style N fill:#c8e6c9
    style D fill:#fff9c4
    style G fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức doPost

```mermaid
flowchart TD
    A[Bắt đầu: doPost] --> B[Set encoding UTF-8<br/>cho request và response]
    B --> C[Lấy session và user]
    C --> D{User tồn tại?}
    D -->|Không| E[Redirect đến /login]
    D -->|Có| F[Lấy dữ liệu từ form:<br/>hoTen, chuyenNganh, namHoc]
    F --> G[Validate và chuẩn hóa dữ liệu:<br/>Loại bỏ khoảng trắng thừa]
    G --> H[Tạo/Update UserProfile object]
    H --> I[Gọi UserProfileDAO.createOrUpdate]
    I --> J{Cập nhật thành công?}
    J -->|Không| K[Set errorMessage vào session]
    J -->|Có| L{hoTen có giá trị?}
    L -->|Có| M[Đồng bộ hoTen sang User<br/>qua UserDAO.updateHoTen]
    M --> N[Cập nhật user trong session]
    N --> O[Set successMessage vào session]
    L -->|Không| O
    K --> P[Lấy lại profile từ database]
    O --> P
    P --> Q[Set profile và user<br/>vào request attribute]
    Q --> R[Forward đến profile.jsp<br/>không redirect để tránh mất dữ liệu]
    R --> S[Hiển thị kết quả]
    S --> T[Kết thúc]
    E --> T
    
    style A fill:#e1f5ff
    style T fill:#c8e6c9
    style D fill:#fff9c4
    style J fill:#fff9c4
    style L fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Hiển Thị Form (doGet)
- Kiểm tra đăng nhập
- Lấy UserProfile từ database, nếu chưa có thì tạo profile rỗng
- Lấy và xóa messages từ session (success/error)
- Hiển thị form với dữ liệu hiện tại

### 2. Cập Nhật Hồ Sơ (doPost)
- Validate và chuẩn hóa dữ liệu đầu vào
- Sử dụng `createOrUpdate` để tạo mới hoặc cập nhật profile
- Đồng bộ họ tên từ profile sang bảng users nếu có thay đổi
- Cập nhật user trong session để phản ánh thay đổi ngay lập tức

### 3. Xử Lý Messages
- Sử dụng session để lưu messages (thành công/lỗi)
- Forward thay vì redirect để giữ nguyên dữ liệu form
- Messages được hiển thị và tự động xóa sau khi lấy

### 4. Đồng Bộ Dữ Liệu
- Khi cập nhật họ tên trong profile, đồng bộ sang bảng users
- Đảm bảo dữ liệu nhất quán giữa hai bảng

