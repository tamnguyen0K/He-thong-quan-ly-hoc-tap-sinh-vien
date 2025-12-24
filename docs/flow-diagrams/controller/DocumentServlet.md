# Sơ Đồ Luồng Hoạt Động - DocumentServlet

## Mô tả
Servlet xử lý quản lý tài liệu của người dùng. Hỗ trợ GET để hiển thị danh sách và POST để xóa tài liệu.

## Sơ Đồ Luồng - Phương Thức doGet

```mermaid
flowchart TD
    A[Bắt đầu: doGet] --> B[Lấy session và user]
    B --> C{User tồn tại?}
    C -->|Không| D[Redirect đến /login]
    C -->|Có| E[Lấy parameter 'monHoc'<br/>để lọc theo môn học]
    E --> F{monHoc được chọn?}
    F -->|Có| G[Lấy documents theo môn học<br/>qua DocumentDAO.findByUserIdAndMonHoc]
    F -->|Không| H[Lấy tất cả documents<br/>qua DocumentDAO.findAllByUserId]
    G --> I[Set documents vào request]
    H --> I
    I --> J[Set selectedMonHoc vào request]
    J --> K[Forward đến documents.jsp]
    K --> L[Hiển thị danh sách tài liệu]
    L --> M[Kết thúc]
    D --> M
    
    style A fill:#e1f5ff
    style M fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức doPost

```mermaid
flowchart TD
    A[Bắt đầu: doPost] --> B[Lấy session và user]
    B --> C{User tồn tại?}
    C -->|Không| D[Redirect đến /login]
    C -->|Có| E[Lấy parameter 'action']
    E --> F{Action là 'delete'?}
    F -->|Không| G[Redirect đến /documents]
    F -->|Có| H[Parse ID từ parameter]
    H --> I{ID hợp lệ?}
    I -->|Không| G
    I -->|Có| J[Tìm document theo ID<br/>qua DocumentDAO.findById]
    J --> K{Document tồn tại và<br/>thuộc về user?}
    K -->|Không| G
    K -->|Có| L[Xóa file vật lý<br/>từ hệ thống file]
    L --> M[Xóa bản ghi<br/>qua DocumentDAO.delete]
    M --> G
    D --> N[Kết thúc]
    G --> N
    
    style A fill:#e1f5ff
    style N fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style I fill:#fff9c4
    style K fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Hiển Thị Danh Sách Tài Liệu (doGet)
- Lấy tất cả tài liệu của user hoặc lọc theo môn học nếu có parameter
- Sắp xếp theo thời gian upload (mới nhất trước)
- Hiển thị danh sách với thông tin: tên file, môn học, kích thước, ngày upload

### 2. Xóa Tài Liệu (doPost)
- Kiểm tra quyền sở hữu document
- Xóa file vật lý từ thư mục uploads
- Xóa bản ghi trong database
- Redirect về trang danh sách tài liệu

### 3. Lọc Theo Môn Học
- Hỗ trợ lọc danh sách tài liệu theo môn học
- Giúp người dùng dễ dàng tìm kiếm tài liệu theo từng môn

