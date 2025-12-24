# Sơ Đồ Luồng Hoạt Động - Document Model

## Mô tả
Model class đại diện cho bảng documents. Lưu trữ thông tin tài liệu của sinh viên.

## Cấu Trúc Dữ Liệu

```mermaid
classDiagram
    class Document {
        -int id
        -int userId
        -String tenFileGoc
        -String tenFileLuu
        -String duongDan
        -String loaiFile
        -long kichThuoc
        -String monHoc
        -String moTa
        -Timestamp uploadedAt
        +Document()
        +getId() int
        +setId(int id)
        +getUserId() int
        +setUserId(int userId)
        +getTenFileGoc() String
        +setTenFileGoc(String ten)
        +getTenFileLuu() String
        +setTenFileLuu(String ten)
        +getDuongDan() String
        +setDuongDan(String duongDan)
        +getLoaiFile() String
        +setLoaiFile(String loai)
        +getKichThuoc() long
        +setKichThuoc(long size)
        +getMonHoc() String
        +setMonHoc(String monHoc)
        +getMoTa() String
        +setMoTa(String moTa)
        +getUploadedAt() Timestamp
        +setUploadedAt(Timestamp time)
    }
```

## Sơ Đồ Luồng - Upload File

```mermaid
flowchart TD
    A[Upload file] --> B[Lấy file từ request]
    B --> C[Tạo Document object]
    C --> D[Set tenFileGoc = tên file gốc]
    D --> E[Tạo tenFileLuu = UUID + extension]
    E --> F[Set duongDan = đường dẫn đầy đủ]
    F --> G[Set loaiFile = extension]
    G --> H[Set kichThuoc = kích thước file]
    H --> I[Lưu file vật lý vào server]
    I --> J[Lưu Document vào database]
    J --> K[Hoàn thành]
    
    style A fill:#e1f5ff
    style K fill:#c8e6c9
```

## Chi Tiết Các Trường

### 1. id (int)
- ID tự động tăng của document
- Được tạo bởi database khi insert

### 2. userId (int)
- ID của user sở hữu document này
- Foreign key tham chiếu đến users.id

### 3. tenFileGoc (String)
- Tên file gốc khi upload
- Giữ nguyên để hiển thị cho user
- Bắt buộc, không được rỗng

### 4. tenFileLuu (String)
- Tên file khi lưu trên server
- Sử dụng UUID để tránh trùng lặp
- Bắt buộc, không được rỗng

### 5. duongDan (String)
- Đường dẫn đầy đủ đến file trên server
- Ví dụ: /uploads/documents/uuid.pdf
- Bắt buộc, không được rỗng

### 6. loaiFile (String)
- Loại file (extension): pdf, doc, docx, txt
- Bắt buộc, không được rỗng

### 7. kichThuoc (long)
- Kích thước file tính bằng bytes
- Bắt buộc, phải > 0

### 8. monHoc (String)
- Môn học liên quan đến document
- Tùy chọn, có thể null

### 9. moTa (String)
- Mô tả về document
- Tùy chọn, có thể null

### 10. uploadedAt (Timestamp)
- Thời gian upload file
- Được set tự động bởi database

## Sơ Đồ Luồng - Download File

```mermaid
flowchart TD
    A[Download file] --> B[Tìm Document theo ID]
    B --> C{Document tồn tại và<br/>thuộc về user?}
    C -->|Không| D[Trả về lỗi 403]
    C -->|Có| E[Kiểm tra file tồn tại trên disk]
    E --> F{File tồn tại?}
    F -->|Không| G[Trả về lỗi 404]
    F -->|Có| H[Set HTTP headers:<br/>Content-Type, Content-Disposition,<br/>Content-Length]
    H --> I[Đọc file từ disk]
    I --> J[Ghi vào Response OutputStream]
    J --> K[Hoàn thành]
    D --> L[Kết thúc]
    G --> L
    K --> L
    
    style A fill:#e1f5ff
    style L fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
```

## Loại File Hỗ Trợ

- PDF (.pdf)
- Word (.doc, .docx)
- Text (.txt)

## Lưu Ý

- File vật lý được lưu trong thư mục uploads/documents
- Tên file lưu sử dụng UUID để tránh trùng lặp và bảo mật
- Khi xóa document, phải xóa cả file vật lý và bản ghi database

