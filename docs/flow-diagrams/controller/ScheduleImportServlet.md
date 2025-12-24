# Sơ Đồ Luồng Hoạt Động - ScheduleImportServlet

## Mô tả
Servlet xử lý import lịch học từ file CSV. Chỉ hỗ trợ POST với multipart/form-data.

## Sơ Đồ Luồng - Phương Thức doPost

```mermaid
flowchart TD
    A[Bắt đầu: doPost] --> B[Lấy session và user]
    B --> C{User tồn tại?}
    C -->|Không| D[Redirect đến /login]
    C -->|Có| E[Lấy file từ request<br/>request.getPart 'file']
    E --> F{File tồn tại và<br/>có kích thước > 0?}
    F -->|Không| G[Redirect đến /schedule<br/>với error: Không có file]
    F -->|Có| H[Mở InputStream và<br/>BufferedReader với UTF-8]
    H --> I[Khởi tạo:<br/>errors list, successCount, errorCount]
    I --> J[Đọc dòng header<br/>và bỏ qua]
    J --> K[Đọc từng dòng CSV]
    K --> L{Dòng còn lại?}
    L -->|Không| M[Tạo message kết quả]
    L -->|Có| N[Parse dòng CSV<br/>qua parseCSVLine]
    N --> O{Dòng có đủ 4 cột?}
    O -->|Không| P[Thêm lỗi: Thiếu thông tin]
    P --> Q[errorCount++]
    O -->|Có| R[Lấy dữ liệu:<br/>tenMon, thuTrongTuan,<br/>thoiGianBatDau, thoiGianKetThuc,<br/>phongHoc, ghiChu]
    R --> S{Validate dữ liệu bắt buộc}
    S -->|Thiếu| P
    S -->|Đủ| T[Parse thời gian<br/>sang Time object]
    T --> U{Parse thành công?}
    U -->|Không| V[Thêm lỗi: Định dạng thời gian sai]
    V --> Q
    U -->|Có| W{Thời gian hợp lệ?<br/>ketThuc > batDau}
    W -->|Không| X[Thêm lỗi: Thời gian không hợp lệ]
    X --> Q
    W -->|Có| Y[Kiểm tra trùng lịch<br/>qua ScheduleDAO.checkConflict]
    Y --> Z{Có trùng lịch?}
    Z -->|Có| AA[Thêm lỗi: Trùng lịch học]
    AA --> Q
    Z -->|Không| AB[Tạo Schedule object]
    AB --> AC[Gọi ScheduleDAO.create]
    AC --> AD{Tạo thành công?}
    AD -->|Có| AE[successCount++]
    AD -->|Không| AF[Thêm lỗi: Lỗi khi lưu]
    AF --> Q
    Q --> K
    AE --> K
    M --> AG{errors.size <= 10?}
    AG -->|Có| AH[Thêm chi tiết lỗi vào message]
    AG -->|Không| AI[Chỉ thêm tổng số lỗi]
    AH --> AJ[Redirect đến /schedule<br/>với message kết quả]
    AI --> AJ
    D --> AK[Kết thúc]
    G --> AK
    AJ --> AK
    
    style A fill:#e1f5ff
    style AK fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style L fill:#fff9c4
    style O fill:#fff9c4
    style S fill:#fff9c4
    style U fill:#fff9c4
    style W fill:#fff9c4
    style Z fill:#fff9c4
    style AD fill:#fff9c4
    style AG fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức parseCSVLine

```mermaid
flowchart TD
    A[Bắt đầu: parseCSVLine] --> B[Khởi tạo:<br/>result list, inQuotes flag,<br/>current StringBuilder]
    B --> C[Duyệt từng ký tự trong dòng]
    C --> D{Ký tự là gì?}
    D -->|Dấu nháy kép "| E{inQuotes?}
    E -->|Có| F[Đóng quotes]
    E -->|Không| G[Mở quotes]
    F --> H[Tiếp tục duyệt]
    G --> H
    D -->|Dấu phẩy ,| I{inQuotes?}
    I -->|Có| J[Thêm vào current<br/>không tách cột]
    I -->|Không| K[Kết thúc cột hiện tại<br/>thêm vào result]
    J --> H
    K --> L[Reset current]
    L --> H
    D -->|Ký tự khác| M[Thêm vào current]
    M --> H
    H --> N{Còn ký tự?}
    N -->|Có| C
    N -->|Không| O[Thêm cột cuối cùng vào result]
    O --> P[Trả về mảng String]
    P --> Q[Kết thúc]
    
    style A fill:#e1f5ff
    style Q fill:#c8e6c9
    style D fill:#fff9c4
    style E fill:#fff9c4
    style I fill:#fff9c4
    style N fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Kiểm Tra File
- Kiểm tra file có tồn tại và có kích thước > 0
- File CSV được đọc với encoding UTF-8

### 2. Parse CSV
- Bỏ qua dòng header
- Đọc từng dòng và parse bằng phương thức `parseCSVLine`
- Hỗ trợ xử lý dấu nháy kép trong CSV (cho phép dấu phẩy trong giá trị)

### 3. Validate Từng Dòng
- Kiểm tra đủ 4 cột bắt buộc (tên môn, thứ, thời gian bắt đầu, thời gian kết thúc)
- Parse thời gian từ string (HH:mm) sang Time object
- Kiểm tra thời gian hợp lệ (kết thúc phải sau bắt đầu)
- Kiểm tra trùng lịch với các lịch đã có

### 4. Lưu Dữ Liệu
- Tạo Schedule object cho mỗi dòng hợp lệ
- Lưu vào database qua ScheduleDAO
- Đếm số dòng thành công và thất bại

### 5. Tổng Hợp Kết Quả
- Tạo message tổng hợp: số dòng thành công, số dòng lỗi
- Nếu có ít hơn 10 lỗi, hiển thị chi tiết từng lỗi
- Nếu có nhiều hơn 10 lỗi, chỉ hiển thị tổng số

### 6. Định Dạng CSV
- Định dạng mong đợi: tenMon, thuTrongTuan, thoiGianBatDau, thoiGianKetThuc, phongHoc, ghiChu
- Hỗ trợ dấu nháy kép để chứa dấu phẩy trong giá trị
- Thời gian định dạng: HH:mm (ví dụ: 08:00, 10:30)

