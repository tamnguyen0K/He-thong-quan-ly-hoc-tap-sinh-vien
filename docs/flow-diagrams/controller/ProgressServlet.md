# Sơ Đồ Luồng Hoạt Động - ProgressServlet

## Mô tả
Servlet xử lý quản lý tiến độ học tập (tasks) của người dùng. Hỗ trợ GET để hiển thị danh sách và POST để thêm/sửa/xóa/toggle trạng thái.

## Sơ Đồ Luồng - Phương Thức doGet

```mermaid
flowchart TD
    A[Bắt đầu: doGet] --> B[Lấy session và user]
    B --> C{User tồn tại?}
    C -->|Không| D[Redirect đến /login]
    C -->|Có| E[Lấy tất cả tasks<br/>qua ProgressDAO.findAllByUserId]
    E --> F[Set tasks vào request]
    F --> G{Lấy parameter 'edit'?}
    G -->|Có| H[Parse edit ID]
    H --> I[Tìm task theo ID]
    I --> J{Task tồn tại và<br/>thuộc về user?}
    J -->|Có| K[Set editTask vào request]
    J -->|Không| L[Forward đến progress.jsp]
    K --> L
    G -->|Không| L
    L --> M[Hiển thị danh sách tasks]
    M --> N[Kết thúc]
    D --> N
    
    style A fill:#e1f5ff
    style N fill:#c8e6c9
    style C fill:#fff9c4
    style G fill:#fff9c4
    style J fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức doPost

```mermaid
flowchart TD
    A[Bắt đầu: doPost] --> B[Lấy session và user]
    B --> C{User tồn tại?}
    C -->|Không| D[Redirect đến /login]
    C -->|Có| E[Lấy parameter 'action']
    E --> F{Action là gì?}
    F -->|add| G[Xử lý thêm task]
    F -->|update| H[Xử lý cập nhật task]
    F -->|toggle| I[Xử lý toggle trạng thái]
    F -->|delete| J[Xử lý xóa task]
    
    G --> K[Lấy message kết quả]
    H --> K
    I --> K
    J --> K
    
    K --> L[Redirect đến /progress<br/>với message trong URL]
    L --> M[Kết thúc]
    D --> M
    
    style A fill:#e1f5ff
    style M fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
```

## Sơ Đồ Luồng - Xử Lý Thêm Task (action=add)

```mermaid
flowchart TD
    A[Bắt đầu: Thêm task] --> B[Tạo Progress object mới]
    B --> C[Lấy dữ liệu từ form:<br/>tenTask, loaiTask, monHoc,<br/>moTa, hanNop]
    C --> D[Parse hanNop từ datetime string<br/>chuyển sang Timestamp]
    D --> E{Parse thành công?}
    E -->|Không| F[Bỏ qua hanNop]
    E -->|Có| G[Set hanNop vào object]
    F --> H[Gọi ProgressDAO.create]
    G --> H
    H --> I{Tạo thành công?}
    I -->|Có| J[Message: Thêm task thành công]
    I -->|Không| K[Message: Lỗi khi thêm task]
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
    style K fill:#ffcdd2
    style E fill:#fff9c4
    style I fill:#fff9c4
```

## Sơ Đồ Luồng - Xử Lý Cập Nhật Task (action=update)

```mermaid
flowchart TD
    A[Bắt đầu: Cập nhật task] --> B[Parse ID từ parameter]
    B --> C{ID hợp lệ?}
    C -->|Không| D[Message: ID không hợp lệ]
    C -->|Có| E[Tìm task theo ID]
    E --> F{Task tồn tại và<br/>thuộc về user?}
    F -->|Không| D
    F -->|Có| G[Lấy dữ liệu mới từ form]
    G --> H[Parse hanNop từ datetime string]
    H --> I{Parse thành công?}
    I -->|Không| J[Bỏ qua hanNop]
    I -->|Có| K[Set hanNop vào object]
    J --> L[Cập nhật các trường khác]
    K --> L
    L --> M[Gọi ProgressDAO.update]
    M --> N{Cập nhật thành công?}
    N -->|Có| O[Message: Cập nhật thành công]
    N -->|Không| P[Message: Lỗi khi cập nhật]
    
    style A fill:#e1f5ff
    style D fill:#ffcdd2
    style P fill:#ffcdd2
    style O fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style I fill:#fff9c4
    style N fill:#fff9c4
```

## Sơ Đồ Luồng - Xử Lý Toggle Trạng Thái (action=toggle)

```mermaid
flowchart TD
    A[Bắt đầu: Toggle trạng thái] --> B[Parse ID từ parameter]
    B --> C{ID hợp lệ?}
    C -->|Không| D[Không có message]
    C -->|Có| E[Gọi ProgressDAO.toggleStatus]
    E --> F{Toggle thành công?}
    F -->|Có| G[Message: Cập nhật trạng thái thành công]
    F -->|Không| D
    
    style A fill:#e1f5ff
    style D fill:#ffcdd2
    style G fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
```

## Sơ Đồ Luồng - Xử Lý Xóa Task (action=delete)

```mermaid
flowchart TD
    A[Bắt đầu: Xóa task] --> B[Parse ID từ parameter]
    B --> C{ID hợp lệ?}
    C -->|Không| D[Không có message]
    C -->|Có| E[Tìm task theo ID]
    E --> F{Task tồn tại và<br/>thuộc về user?}
    F -->|Không| D
    F -->|Có| G[Gọi ProgressDAO.delete]
    G --> H{Xóa thành công?}
    H -->|Có| I[Message: Xóa thành công]
    H -->|Không| D
    
    style A fill:#e1f5ff
    style D fill:#ffcdd2
    style I fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style H fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Hiển Thị Danh Sách Tasks (doGet)
- Lấy tất cả tasks của user, sắp xếp theo hạn nộp
- Nếu có parameter `edit`, lấy task tương ứng để hiển thị trong form chỉnh sửa

### 2. Thêm Task
- Tạo Progress object mới với thông tin từ form
- Parse hạn nộp từ datetime string sang Timestamp
- Trạng thái mặc định là "chuaxong"

### 3. Cập Nhật Task
- Kiểm tra task tồn tại và thuộc về user
- Cập nhật các trường thông tin
- Parse lại hạn nộp nếu có

### 4. Toggle Trạng Thái
- Chuyển đổi trạng thái giữa "chuaxong" và "daxong"
- Tự động cập nhật completed_at khi chuyển sang "daxong"

### 5. Xóa Task
- Kiểm tra quyền sở hữu
- Xóa task khỏi database

