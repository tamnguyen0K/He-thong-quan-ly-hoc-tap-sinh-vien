# Sơ Đồ Luồng Hoạt Động - FileUtil

## Mô tả
Utility class để xử lý file upload và quản lý file trên server.

## Sơ Đồ Luồng - Phương Thức getFileExtension

```mermaid
flowchart TD
    A[Bắt đầu: getFileExtension] --> B[Nhận fileName]
    B --> C{fileName == null?}
    C -->|Có| D[Trả về chuỗi rỗng]
    C -->|Không| E[Tìm vị trí dấu chấm cuối cùng]
    E --> F{Có dấu chấm?}
    F -->|Không| D
    F -->|Có| G[Lấy phần sau dấu chấm]
    G --> H[Chuyển sang chữ thường]
    H --> I[Trả về extension]
    D --> J[Kết thúc]
    I --> J
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức isAllowedFileType

```mermaid
flowchart TD
    A[Bắt đầu: isAllowedFileType] --> B[Nhận fileName]
    B --> C[Lấy extension qua getFileExtension]
    C --> D{extension là gì?}
    D -->|pdf| E[Trả về true]
    D -->|doc| E
    D -->|docx| E
    D -->|txt| E
    D -->|Khác| F[Trả về false]
    E --> G[Kết thúc]
    F --> G
    
    style A fill:#e1f5ff
    style G fill:#c8e6c9
    style D fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức generateUniqueFileName

```mermaid
flowchart TD
    A[Bắt đầu: generateUniqueFileName] --> B[Nhận originalName]
    B --> C[Lấy extension qua getFileExtension]
    C --> D[Tạo UUID ngẫu nhiên]
    D --> E{extension rỗng?}
    E -->|Có| F[Trả về UUID]
    E -->|Không| G[Trả về UUID + '.' + extension]
    F --> H[Kết thúc]
    G --> H
    
    style A fill:#e1f5ff
    style H fill:#c8e6c9
    style E fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức getUploadDirectory

```mermaid
flowchart TD
    A[Bắt đầu: getUploadDirectory] --> B[Nhận ServletContext]
    B --> C[Lấy real path:<br/>context.getRealPath '/uploads/documents']
    C --> D[Tạo File object từ path]
    D --> E{Thư mục tồn tại?}
    E -->|Không| F[Tạo thư mục<br/>dir.mkdirs]
    E -->|Có| G[Trả về đường dẫn]
    F --> G
    G --> H[Kết thúc]
    
    style A fill:#e1f5ff
    style H fill:#c8e6c9
    style E fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức saveFile

```mermaid
flowchart TD
    A[Bắt đầu: saveFile] --> B[Nhận InputStream, fileName, uploadDir]
    B --> C[Tạo Path từ uploadDir và fileName]
    C --> D[Copy InputStream vào filePath<br/>Files.copy với REPLACE_EXISTING]
    D --> E{Copy thành công?}
    E -->|Có| F[Trả về true]
    E -->|Không| G[In lỗi ra console]
    G --> H[Trả về false]
    F --> I[Kết thúc]
    H --> I
    
    style A fill:#e1f5ff
    style I fill:#c8e6c9
    style E fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức formatFileSize

```mermaid
flowchart TD
    A[Bắt đầu: formatFileSize] --> B[Nhận size tính bằng bytes]
    B --> C{size < 1024?}
    C -->|Có| D[Trả về size + ' B']
    C -->|Không| E{size < 1024 * 1024?}
    E -->|Có| F[Trả về size / 1024.0 + ' KB']
    E -->|Không| G[Trả về size / 1024.0 / 1024.0 + ' MB']
    D --> H[Kết thúc]
    F --> H
    G --> H
    
    style A fill:#e1f5ff
    style H fill:#c8e6c9
    style C fill:#fff9c4
    style E fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Lấy Extension
- Tìm dấu chấm cuối cùng trong tên file
- Lấy phần sau dấu chấm
- Chuyển sang chữ thường để so sánh

### 2. Kiểm Tra Loại File
- Chỉ cho phép: pdf, doc, docx, txt
- Các loại file khác bị từ chối

### 3. Tạo Tên File Duy Nhất
- Sử dụng UUID để tạo tên file ngẫu nhiên
- Giữ nguyên extension của file gốc
- Đảm bảo không trùng lặp

### 4. Quản Lý Thư Mục Upload
- Thư mục: /uploads/documents
- Tự động tạo thư mục nếu chưa tồn tại
- Sử dụng ServletContext để lấy real path

### 5. Lưu File
- Sử dụng Files.copy() để copy InputStream vào file
- REPLACE_EXISTING: Ghi đè nếu file đã tồn tại
- Xử lý exception và trả về boolean

### 6. Format Kích Thước
- < 1024 bytes: Hiển thị B
- < 1024 KB: Hiển thị KB (2 chữ số thập phân)
- >= 1024 KB: Hiển thị MB (2 chữ số thập phân)

