# Sơ Đồ Luồng Hoạt Động - ScheduleServlet

## Mô tả
Servlet xử lý CRUD lịch học của người dùng. Hỗ trợ GET để hiển thị danh sách và POST để thêm/sửa/xóa.

## Sơ Đồ Luồng - Phương Thức doGet

```mermaid
flowchart TD
    A[Bắt đầu: doGet] --> B[Lấy session và user]
    B --> C{User tồn tại?}
    C -->|Không| D[Redirect đến /login]
    C -->|Có| E[Lấy tất cả lịch học<br/>qua ScheduleDAO.findAllByUserId]
    E --> F[Set schedules vào request]
    F --> G{Lấy parameter 'edit'?}
    G -->|Có| H[Parse edit ID]
    H --> I[Tìm schedule theo ID]
    I --> J{Schedule tồn tại và<br/>thuộc về user?}
    J -->|Có| K[Set editSchedule vào request]
    J -->|Không| L[Forward đến schedule.jsp]
    K --> L
    G -->|Không| L
    L --> M[Hiển thị danh sách lịch học]
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
    F -->|add| G[Gọi handleAdd]
    F -->|update| H[Gọi handleUpdate]
    F -->|delete| I[Gọi handleDelete]
    
    G --> J[Lấy message kết quả]
    H --> J
    I --> J
    
    J --> K{Message tồn tại?}
    K -->|Có| L[Redirect đến /schedule<br/>với message trong URL]
    K -->|Không| M[Redirect đến /schedule]
    L --> N[Kết thúc]
    M --> N
    D --> N
    
    style A fill:#e1f5ff
    style N fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style K fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức handleAdd

```mermaid
flowchart TD
    A[Bắt đầu: handleAdd] --> B[Lấy dữ liệu từ form:<br/>tenMon, thuTrongTuan,<br/>thoiGianBatDau, thoiGianKetThuc,<br/>phongHoc, ghiChu]
    B --> C{Validate dữ liệu bắt buộc}
    C -->|Thiếu| D[Trả về: Vui lòng điền đầy đủ]
    C -->|Đủ| E[Chuyển đổi thời gian<br/>sang Time object]
    E --> F{Thời gian hợp lệ?<br/>ketThuc > batDau}
    F -->|Không| G[Trả về: Thời gian không hợp lệ]
    F -->|Có| H[Kiểm tra trùng lịch<br/>qua ScheduleDAO.checkConflict]
    H --> I{Có trùng lịch?}
    I -->|Có| J[Trả về: Lịch học bị trùng]
    I -->|Không| K[Tạo Schedule object]
    K --> L[Gọi ScheduleDAO.create]
    L --> M{Tạo thành công?}
    M -->|Có| N[Trả về: Thêm lịch học thành công]
    M -->|Không| O[Trả về: Có lỗi xảy ra]
    
    style A fill:#e1f5ff
    style D fill:#ffcdd2
    style G fill:#ffcdd2
    style J fill:#ffcdd2
    style O fill:#ffcdd2
    style N fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style I fill:#fff9c4
    style M fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức handleUpdate

```mermaid
flowchart TD
    A[Bắt đầu: handleUpdate] --> B[Lấy ID từ parameter]
    B --> C{ID hợp lệ?}
    C -->|Không| D[Trả về: Thiếu ID]
    C -->|Có| E[Tìm schedule theo ID]
    E --> F{Schedule tồn tại và<br/>thuộc về user?}
    F -->|Không| G[Trả về: Không tìm thấy]
    F -->|Có| H[Lấy dữ liệu mới từ form]
    H --> I{Validate dữ liệu}
    I -->|Thiếu| J[Trả về: Vui lòng điền đầy đủ]
    I -->|Đủ| K[Chuyển đổi thời gian]
    K --> L{Thời gian hợp lệ?}
    L -->|Không| M[Trả về: Thời gian không hợp lệ]
    L -->|Có| N[Kiểm tra trùng lịch<br/>loại trừ ID hiện tại]
    N --> O{Có trùng?}
    O -->|Có| P[Trả về: Lịch học bị trùng]
    O -->|Không| Q[Cập nhật schedule object]
    Q --> R[Gọi ScheduleDAO.update]
    R --> S{Cập nhật thành công?}
    S -->|Có| T[Trả về: Cập nhật thành công]
    S -->|Không| U[Trả về: Có lỗi xảy ra]
    
    style A fill:#e1f5ff
    style D fill:#ffcdd2
    style G fill:#ffcdd2
    style J fill:#ffcdd2
    style M fill:#ffcdd2
    style P fill:#ffcdd2
    style U fill:#ffcdd2
    style T fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style I fill:#fff9c4
    style L fill:#fff9c4
    style O fill:#fff9c4
    style S fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức handleDelete

```mermaid
flowchart TD
    A[Bắt đầu: handleDelete] --> B[Lấy ID từ parameter]
    B --> C{ID hợp lệ?}
    C -->|Không| D[Trả về: Thiếu ID]
    C -->|Có| E[Tìm schedule theo ID]
    E --> F{Schedule tồn tại và<br/>thuộc về user?}
    F -->|Không| G[Trả về: Không tìm thấy]
    F -->|Có| H[Gọi ScheduleDAO.delete]
    H --> I{Xóa thành công?}
    I -->|Có| J[Trả về: Xóa lịch học thành công]
    I -->|Không| K[Trả về: Có lỗi xảy ra]
    
    style A fill:#e1f5ff
    style D fill:#ffcdd2
    style G fill:#ffcdd2
    style K fill:#ffcdd2
    style J fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style I fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Hiển Thị Danh Sách (doGet)
- Lấy tất cả lịch học của user, sắp xếp theo thứ và thời gian
- Nếu có parameter `edit`, lấy schedule tương ứng để hiển thị trong form chỉnh sửa

### 2. Thêm Lịch Học (handleAdd)
- Validate dữ liệu bắt buộc
- Kiểm tra thời gian hợp lệ (kết thúc phải sau bắt đầu)
- Kiểm tra trùng lịch với các lịch đã có
- Tạo mới nếu hợp lệ

### 3. Cập Nhật Lịch Học (handleUpdate)
- Kiểm tra schedule tồn tại và thuộc về user
- Validate dữ liệu mới
- Kiểm tra trùng lịch (loại trừ chính nó)
- Cập nhật nếu hợp lệ

### 4. Xóa Lịch Học (handleDelete)
- Kiểm tra schedule tồn tại và thuộc về user
- Xóa khỏi database

### 5. Bảo Mật
- Tất cả các thao tác đều kiểm tra schedule thuộc về user hiện tại
- Ngăn chặn người dùng chỉnh sửa/xóa lịch của người khác

