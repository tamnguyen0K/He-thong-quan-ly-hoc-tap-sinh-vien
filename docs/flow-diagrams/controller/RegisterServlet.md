# Sơ Đồ Luồng Hoạt Động - RegisterServlet

## Mô tả
Servlet xử lý đăng ký tài khoản người dùng mới. Hỗ trợ GET để hiển thị form đăng ký và POST để xử lý đăng ký.

## Sơ Đồ Luồng - Phương Thức doGet

```mermaid
flowchart TD
    A[Bắt đầu: doGet] --> B[Lấy Request và Response]
    B --> C[Forward đến register.jsp]
    C --> D[Hiển thị form đăng ký]
    D --> E[Kết thúc]
    
    style A fill:#e1f5ff
    style E fill:#c8e6c9
```

## Sơ Đồ Luồng - Phương Thức doPost

```mermaid
flowchart TD
    A[Bắt đầu: doPost] --> B[Lấy dữ liệu từ form:<br/>hoTen, email, password, confirmPassword]
    B --> C{Validate dữ liệu}
    C -->|Họ tên rỗng| D1[Thiết lập lỗi: Họ và tên không được để trống]
    C -->|Email rỗng| D2[Thiết lập lỗi: Email không được để trống]
    C -->|Email không hợp lệ| D3[Thiết lập lỗi: Email không hợp lệ]
    C -->|Mật khẩu rỗng| D4[Thiết lập lỗi: Mật khẩu không được để trống]
    C -->|Mật khẩu < 6 ký tự| D5[Thiết lập lỗi: Mật khẩu phải có ít nhất 6 ký tự]
    C -->|Mật khẩu không khớp| D6[Thiết lập lỗi: Mật khẩu và xác nhận không khớp]
    C -->|Hợp lệ| E[Kiểm tra email đã tồn tại chưa]
    
    D1 --> F{Có lỗi?}
    D2 --> F
    D3 --> F
    D4 --> F
    D5 --> F
    D6 --> F
    
    F -->|Có| G[Set error vào request]
    G --> H[Forward lại register.jsp với lỗi]
    H --> I[Kết thúc]
    
    E --> J{Email đã tồn tại?}
    J -->|Có| K[Thiết lập lỗi: Email đã được sử dụng]
    K --> F
    
    J -->|Không| L[Hash password bằng PasswordUtil]
    L --> M[Tạo User mới với role='student']
    M --> N[Tạo User trong database qua UserDAO.create]
    N --> O{User được tạo thành công?}
    
    O -->|Không| P[Thiết lập lỗi: Có lỗi xảy ra khi đăng ký]
    P --> F
    
    O -->|Có| Q[Tạo UserProfile với hoTen]
    Q --> R[Lưu UserProfile vào database]
    R --> S[Redirect đến /login với thông báo thành công]
    S --> I
    
    style A fill:#e1f5ff
    style I fill:#c8e6c9
    style F fill:#fff9c4
    style O fill:#fff9c4
    style J fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Validate Dữ Liệu
- Kiểm tra họ tên không được rỗng
- Kiểm tra email không được rỗng và đúng định dạng
- Kiểm tra mật khẩu không được rỗng, tối thiểu 6 ký tự
- Kiểm tra mật khẩu và xác nhận mật khẩu phải khớp

### 2. Kiểm Tra Email Trùng
- Sử dụng `UserDAO.findByEmail()` để kiểm tra email đã tồn tại

### 3. Tạo Tài Khoản
- Hash mật khẩu bằng `PasswordUtil.hashPassword()`
- Tạo User mới với role mặc định là "student"
- Lưu User vào database
- Tạo UserProfile tương ứng để đồng bộ thông tin

### 4. Kết Quả
- Thành công: Redirect đến trang đăng nhập với thông báo
- Thất bại: Hiển thị lại form với thông báo lỗi

