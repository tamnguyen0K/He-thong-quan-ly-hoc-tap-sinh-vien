# Sơ Đồ Luồng Hoạt Động - UploadDocumentServlet

## Mô tả
Servlet xử lý upload tài liệu lên server. Chỉ hỗ trợ POST với multipart/form-data.

## Sơ Đồ Luồng - Phương Thức doPost

```mermaid
flowchart TD
    A[Bắt đầu: doPost] --> B[Lấy session và user]
    B --> C{User tồn tại?}
    C -->|Không| D[Redirect đến /login]
    C -->|Có| E[Lấy file từ request<br/>request.getPart 'file']
    E --> F{File tồn tại và<br/>có kích thước > 0?}
    F -->|Không| G[Redirect đến /documents<br/>với error: Không có file]
    F -->|Có| H[Lấy tên file gốc]
    H --> I[Kiểm tra loại file<br/>qua FileUtil.isAllowedFileType]
    I --> J{File hợp lệ?<br/>PDF, DOC, DOCX, TXT}
    J -->|Không| K[Redirect đến /documents<br/>với error: File không hợp lệ]
    J -->|Có| L{Kích thước file<br/><= 10MB?}
    L -->|Không| M[Redirect đến /documents<br/>với error: File quá lớn]
    L -->|Có| N[Tạo tên file duy nhất<br/>qua FileUtil.generateUniqueFileName]
    N --> O[Lấy thư mục upload<br/>qua FileUtil.getUploadDirectory]
    O --> P[Tạo đường dẫn file đầy đủ]
    P --> Q[Lưu file vào server<br/>qua FileUtil.saveFile]
    Q --> R{Lưu thành công?}
    R -->|Không| S[Redirect đến /documents<br/>với error: Lỗi khi upload]
    R -->|Có| T[Tạo Document object]
    T --> U[Set thông tin:<br/>userId, tenFileGoc, tenFileLuu,<br/>duongDan, loaiFile, kichThuoc,<br/>monHoc, moTa]
    U --> V[Lưu vào database<br/>qua DocumentDAO.create]
    V --> W{Lưu thành công?}
    W -->|Không| X[Xóa file vật lý đã upload]
    X --> Y[Redirect đến /documents<br/>với error: Lỗi khi lưu]
    W -->|Có| Z[Redirect đến /documents<br/>với message: Upload thành công]
    D --> AA[Kết thúc]
    G --> AA
    K --> AA
    M --> AA
    S --> AA
    Y --> AA
    Z --> AA
    
    style A fill:#e1f5ff
    style AA fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style J fill:#fff9c4
    style L fill:#fff9c4
    style R fill:#fff9c4
    style W fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Kiểm Tra Đăng Nhập
- Xác thực user đã đăng nhập trước khi cho phép upload

### 2. Validate File
- Kiểm tra file có tồn tại và có kích thước > 0
- Kiểm tra loại file chỉ cho phép: PDF, DOC, DOCX, TXT
- Kiểm tra kích thước file tối đa 10MB

### 3. Xử Lý File
- Tạo tên file duy nhất bằng UUID để tránh trùng lặp
- Lấy thư mục upload từ ServletContext
- Lưu file vào thư mục uploads/documents

### 4. Lưu Thông Tin Database
- Tạo Document object với đầy đủ thông tin
- Lưu vào database qua DocumentDAO
- Nếu lưu database thất bại, xóa file vật lý đã upload

### 5. Kết Quả
- Thành công: Redirect với thông báo thành công
- Thất bại: Redirect với thông báo lỗi cụ thể

### 6. Bảo Mật
- Chỉ cho phép upload các loại file an toàn
- Giới hạn kích thước file để tránh tấn công DoS
- Tên file được tạo ngẫu nhiên để tránh truy cập trực tiếp

