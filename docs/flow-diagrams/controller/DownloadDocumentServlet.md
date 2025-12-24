# Sơ Đồ Luồng Hoạt Động - DownloadDocumentServlet

## Mô tả
Servlet xử lý download tài liệu từ server. Chỉ hỗ trợ GET, yêu cầu đăng nhập và kiểm tra quyền sở hữu.

## Sơ Đồ Luồng - Phương Thức doGet

```mermaid
flowchart TD
    A[Bắt đầu: doGet] --> B[Lấy session và user]
    B --> C{User tồn tại?}
    C -->|Không| D[Send error 401<br/>Unauthorized]
    C -->|Có| E[Lấy parameter 'id']
    E --> F{ID tồn tại?}
    F -->|Không| G[Send error 400<br/>Bad Request]
    F -->|Có| H[Parse ID thành integer]
    H --> I{Parse thành công?}
    I -->|Không| G
    I -->|Có| J[Tìm document theo ID<br/>qua DocumentDAO.findById]
    J --> K{Document tồn tại?}
    K -->|Không| L[Send error 403<br/>Forbidden]
    K -->|Có| M{Document thuộc về user?}
    M -->|Không| L
    M -->|Có| N[Tạo File object<br/>từ đường dẫn]
    N --> O{File tồn tại trên disk?}
    O -->|Không| P[Send error 404<br/>Not Found]
    O -->|Có| Q[Set Content-Type:<br/>application/octet-stream]
    Q --> R[Set Content-Disposition:<br/>attachment với tên file gốc]
    R --> S[Set Content-Length:<br/>kích thước file]
    S --> T[Mở FileInputStream]
    T --> U[Đọc file theo buffer 4KB]
    U --> V[Ghi vào Response OutputStream]
    V --> W{Đọc hết file?}
    W -->|Chưa| U
    W -->|Có| X[Đóng streams]
    X --> Y[Kết thúc]
    D --> Y
    G --> Y
    L --> Y
    P --> Y
    
    style A fill:#e1f5ff
    style Y fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style I fill:#fff9c4
    style K fill:#fff9c4
    style M fill:#fff9c4
    style O fill:#fff9c4
    style W fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Kiểm Tra Đăng Nhập
- Xác thực user đã đăng nhập, nếu không trả về 401 Unauthorized

### 2. Validate ID
- Kiểm tra parameter ID có tồn tại
- Parse ID thành integer, nếu không hợp lệ trả về 400 Bad Request

### 3. Kiểm Tra Quyền Sở Hữu
- Tìm document theo ID trong database
- Kiểm tra document thuộc về user hiện tại
- Nếu không tìm thấy hoặc không thuộc về user, trả về 403 Forbidden

### 4. Kiểm Tra File Vật Lý
- Tạo File object từ đường dẫn lưu trong database
- Kiểm tra file có tồn tại trên disk
- Nếu không tồn tại, trả về 404 Not Found

### 5. Stream File
- Set các HTTP headers phù hợp:
  - Content-Type: application/octet-stream
  - Content-Disposition: attachment với tên file gốc
  - Content-Length: kích thước file
- Đọc file theo buffer 4KB để tối ưu bộ nhớ
- Ghi từng buffer vào Response OutputStream
- Đóng streams sau khi hoàn thành

### 6. Bảo Mật
- Chỉ cho phép download file của chính user
- Kiểm tra file tồn tại trước khi stream
- Sử dụng tên file gốc trong header để người dùng nhận được tên file đúng

