# Sơ Đồ Luồng Hoạt Động - GradeServlet

## Mô tả
Servlet xử lý quản lý điểm số của người dùng. Hỗ trợ GET để hiển thị danh sách điểm và POST để thêm/sửa/xóa điểm.

## Sơ Đồ Luồng - Phương Thức doGet

```mermaid
flowchart TD
    A[Bắt đầu: doGet] --> B[Lấy session và user]
    B --> C{User tồn tại?}
    C -->|Không| D[Redirect đến /login]
    C -->|Có| E[Lấy parameter 'term'<br/>học kỳ để lọc]
    E --> F[Lấy tất cả điểm<br/>qua GradeDAO.findAllByUserId]
    F --> G[Lọc điểm chi tiết<br/>loại trừ điểm tổng kết]
    G --> H{term được chọn?}
    H -->|Có| I[Lọc theo học kỳ]
    H -->|Không| J[Set grades vào request]
    I --> J
    J --> K[Lấy điểm tổng kết<br/>qua GPAService.getFinalGrades]
    K --> L{term được chọn?}
    L -->|Có| M[Lọc điểm tổng kết theo học kỳ]
    L -->|Không| N[Set finalGrades vào request]
    M --> N
    N --> O[Tính điểm hệ 4 cho từng môn]
    O --> P[Tính GPA tổng hệ 4 và hệ 10]
    P --> Q[Lấy danh sách học kỳ và môn học]
    Q --> R{Lấy parameter 'edit'?}
    R -->|Có| S[Tìm grade theo ID]
    S --> T{Grade tồn tại và<br/>thuộc về user?}
    T -->|Có| U[Set editGrade vào request]
    T -->|Không| V[Forward đến grades.jsp]
    U --> V
    R -->|Không| V
    V --> W[Hiển thị bảng điểm]
    W --> X[Kết thúc]
    D --> X
    
    style A fill:#e1f5ff
    style X fill:#c8e6c9
    style C fill:#fff9c4
    style H fill:#fff9c4
    style L fill:#fff9c4
    style R fill:#fff9c4
    style T fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức doPost

```mermaid
flowchart TD
    A[Bắt đầu: doPost] --> B[Lấy session và user]
    B --> C{User tồn tại?}
    C -->|Không| D[Redirect đến /login]
    C -->|Có| E[Lấy parameter 'action']
    E --> F{Action là gì?}
    F -->|add| G[Xử lý thêm điểm]
    F -->|update| H[Xử lý cập nhật điểm]
    F -->|delete| I[Xử lý xóa điểm]
    
    G --> J[Lấy message kết quả]
    H --> J
    I --> J
    
    J --> K[Redirect đến /grades<br/>với message trong URL]
    K --> L[Kết thúc]
    D --> L
    
    style A fill:#e1f5ff
    style L fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
```

## Sơ Đồ Luồng - Xử Lý Thêm Điểm (action=add)

```mermaid
flowchart TD
    A[Bắt đầu: Thêm điểm] --> B[Tạo Grade object mới]
    B --> C[Lấy dữ liệu từ form:<br/>tenMon, namHoc, hocKy,<br/>tinChi, diemSo, loaiDiem, ghiChu]
    C --> D{Parse tinChi và diemSo<br/>thành công?}
    D -->|Không| E[Message: Dữ liệu không hợp lệ]
    D -->|Có| F[Gọi GradeDAO.create]
    F --> G{Tạo thành công?}
    G -->|Không| H[Message: Lỗi khi thêm điểm]
    G -->|Có| I{Môn học đã có đủ<br/>3 loại điểm?<br/>quatrinh, giuaky, ketthuc}
    I -->|Có| J[Tự động tính và lưu<br/>điểm tổng kết<br/>qua GPAService]
    I -->|Không| K[Message: Thêm điểm thành công]
    J --> K
    
    style A fill:#e1f5ff
    style E fill:#ffcdd2
    style H fill:#ffcdd2
    style K fill:#c8e6c9
    style D fill:#fff9c4
    style G fill:#fff9c4
    style I fill:#fff9c4
```

## Sơ Đồ Luồng - Xử Lý Cập Nhật Điểm (action=update)

```mermaid
flowchart TD
    A[Bắt đầu: Cập nhật điểm] --> B[Parse ID từ parameter]
    B --> C{ID hợp lệ?}
    C -->|Không| D[Message: Lỗi khi cập nhật]
    C -->|Có| E[Tìm grade theo ID]
    E --> F{Grade tồn tại và<br/>thuộc về user?}
    F -->|Không| D
    F -->|Có| G[Lưu thông tin cũ:<br/>oldTenMon, oldNamHoc, oldHocKy]
    G --> H[Lấy dữ liệu mới từ form]
    H --> I[Cập nhật grade object]
    I --> J[Gọi GradeDAO.update]
    J --> K{Cập nhật thành công?}
    K -->|Không| D
    K -->|Có| L{Môn học có thay đổi?<br/>tenMon, namHoc, hocKy}
    L -->|Có| M[Tính lại điểm tổng kết<br/>cho môn học cũ]
    M --> N[Tính lại điểm tổng kết<br/>cho môn học mới]
    L -->|Không| O{Môn học mới đã có<br/>đủ 3 loại điểm?}
    N --> O
    O -->|Có| P[Tính và lưu điểm tổng kết<br/>cho môn học mới]
    O -->|Không| Q[Message: Cập nhật thành công]
    P --> Q
    
    style A fill:#e1f5ff
    style D fill:#ffcdd2
    style Q fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style K fill:#fff9c4
    style L fill:#fff9c4
    style O fill:#fff9c4
```

## Sơ Đồ Luồng - Xử Lý Xóa Điểm (action=delete)

```mermaid
flowchart TD
    A[Bắt đầu: Xóa điểm] --> B[Parse ID từ parameter]
    B --> C{ID hợp lệ?}
    C -->|Không| D[Message: ID không hợp lệ]
    C -->|Có| E[Tìm grade theo ID]
    E --> F{Grade tồn tại và<br/>thuộc về user?}
    F -->|Không| D
    F -->|Có| G[Lưu thông tin môn học:<br/>tenMon, namHoc, hocKy]
    G --> H[Gọi GradeDAO.delete]
    H --> I{Xóa thành công?}
    I -->|Không| D
    I -->|Có| J[Tìm điểm tổng kết<br/>của môn học này]
    J --> K{Điểm tổng kết tồn tại?}
    K -->|Có| L[Xóa điểm tổng kết]
    K -->|Không| M{Môn học vẫn còn<br/>đủ 3 loại điểm?}
    L --> M
    M -->|Có| N[Tính lại điểm tổng kết]
    M -->|Không| O[Message: Xóa thành công]
    N --> O
    
    style A fill:#e1f5ff
    style D fill:#ffcdd2
    style O fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style I fill:#fff9c4
    style K fill:#fff9c4
    style M fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Hiển Thị Danh Sách Điểm (doGet)
- Lấy tất cả điểm của user
- Phân loại: điểm chi tiết (quá trình, giữa kỳ, kết thúc) và điểm tổng kết
- Tính điểm hệ 4 cho từng môn và GPA tổng
- Hỗ trợ lọc theo học kỳ

### 2. Thêm Điểm
- Validate và parse dữ liệu đầu vào
- Tạo điểm mới trong database
- Tự động tính và lưu điểm tổng kết nếu môn học đã có đủ 3 loại điểm

### 3. Cập Nhật Điểm
- Kiểm tra quyền sở hữu
- Cập nhật điểm trong database
- Nếu môn học thay đổi, tính lại điểm tổng kết cho cả môn cũ và môn mới
- Nếu môn học mới đủ 3 loại điểm, tự động tính điểm tổng kết

### 4. Xóa Điểm
- Kiểm tra quyền sở hữu
- Xóa điểm khỏi database
- Xóa điểm tổng kết tương ứng nếu có
- Nếu môn học vẫn còn đủ 3 loại điểm, tính lại điểm tổng kết

### 5. Tính Điểm Tự Động
- Hệ thống tự động tính điểm tổng kết khi:
  - Thêm điểm và môn học đã có đủ 3 loại
  - Cập nhật điểm và môn học mới đủ 3 loại
  - Xóa điểm nhưng môn học vẫn còn đủ 3 loại

