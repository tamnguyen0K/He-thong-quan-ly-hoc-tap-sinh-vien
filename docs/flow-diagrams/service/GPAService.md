# Sơ Đồ Luồng Hoạt Động - GPAService

## Mô tả
Service tính toán GPA (Grade Point Average) cho sinh viên. Xử lý tính điểm tổng kết và chuyển đổi điểm hệ 10 sang hệ 4.

## Sơ Đồ Luồng - Phương Thức calculateFinalGrade

```mermaid
flowchart TD
    A[Bắt đầu: calculateFinalGrade] --> B[Nhận 3 tham số:<br/>diemQuaTrinh, diemGiuaKy, diemKetThuc]
    B --> C[Khởi tạo diemTongKet = 0]
    C --> D{diemQuaTrinh != null?}
    D -->|Có| E[diemTongKet += diemQuaTrinh * 0.2]
    D -->|Không| F{diemGiuaKy != null?}
    E --> F
    F -->|Có| G[diemTongKet += diemGiuaKy * 0.3]
    F -->|Không| H{diemKetThuc != null?}
    G --> H
    H -->|Có| I[diemTongKet += diemKetThuc * 0.5]
    H -->|Không| J[Làm tròn 2 chữ số thập phân<br/>RoundingMode.HALF_UP]
    I --> J
    J --> K[Trả về diemTongKet]
    K --> L[Kết thúc]
    
    style A fill:#e1f5ff
    style L fill:#c8e6c9
    style D fill:#fff9c4
    style F fill:#fff9c4
    style H fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức convertToGPA4

```mermaid
flowchart TD
    A[Bắt đầu: convertToGPA4] --> B[Nhận diem10 BigDecimal]
    B --> C{diem10 == null?}
    C -->|Có| D[Trả về 0.0]
    C -->|Không| E[Chuyển sang double]
    E --> F{diem >= 9.0?}
    F -->|Có| G[Trả về 4.0]
    F -->|Không| H{diem >= 8.0?}
    H -->|Có| I[Trả về 3.5]
    H -->|Không| J{diem >= 7.0?}
    J -->|Có| K[Trả về 3.0]
    J -->|Không| L{diem >= 6.5?}
    L -->|Có| M[Trả về 2.5]
    L -->|Không| N{diem >= 5.5?}
    N -->|Có| O[Trả về 2.0]
    N -->|Không| P{diem >= 5.0?}
    P -->|Có| Q[Trả về 1.5]
    P -->|Không| R{diem >= 4.0?}
    R -->|Có| S[Trả về 1.0]
    R -->|Không| T[Trả về 0.0]
    D --> U[Kết thúc]
    G --> U
    I --> U
    K --> U
    M --> U
    O --> U
    Q --> U
    S --> U
    T --> U
    
    style A fill:#e1f5ff
    style U fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
    style H fill:#fff9c4
    style J fill:#fff9c4
    style L fill:#fff9c4
    style N fill:#fff9c4
    style P fill:#fff9c4
    style R fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức calculateAndSaveFinalGrade

```mermaid
flowchart TD
    A[Bắt đầu: calculateAndSaveFinalGrade] --> B[Nhận tham số:<br/>userId, tenMon, namHoc, hocKy]
    B --> C[Lấy 3 loại điểm từ database:<br/>quatrinh, giuaky, ketthuc]
    C --> D{Đủ 3 loại điểm?}
    D -->|Không| E[Kết thúc - không làm gì]
    D -->|Có| F[Tính điểm tổng kết<br/>qua calculateFinalGrade]
    F --> G[Tìm điểm tổng kết hiện có]
    G --> H{Điểm tổng kết tồn tại?}
    H -->|Có| I[Cập nhật điểm tổng kết]
    H -->|Không| J[Tạo điểm tổng kết mới]
    I --> K[Lưu vào database]
    J --> K
    K --> L[Kết thúc]
    E --> L
    
    style A fill:#e1f5ff
    style L fill:#c8e6c9
    style D fill:#fff9c4
    style H fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức calculateOverallGPA4

```mermaid
flowchart TD
    A[Bắt đầu: calculateOverallGPA4] --> B[Lấy tất cả điểm của user]
    B --> C[Khởi tạo:<br/>totalDiem4 = 0, totalTinChi = 0]
    C --> D[Lặp qua từng điểm]
    D --> E{loaiDiem == 'tongket'?}
    E -->|Không| D
    E -->|Có| F[Chuyển điểm sang hệ 4<br/>qua convertToGPA4]
    F --> G[totalDiem4 += diem4 * tinChi]
    G --> H[totalTinChi += tinChi]
    H --> D
    D --> I{totalTinChi == 0?}
    I -->|Có| J[Trả về 0.0]
    I -->|Không| K[GPA4 = totalDiem4 / totalTinChi]
    K --> L[Làm tròn 2 chữ số thập phân]
    L --> M[Trả về GPA4]
    J --> N[Kết thúc]
    M --> N
    
    style A fill:#e1f5ff
    style N fill:#c8e6c9
    style E fill:#fff9c4
    style I fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức calculateOverallGPA10

```mermaid
flowchart TD
    A[Bắt đầu: calculateOverallGPA10] --> B[Lấy tất cả điểm của user]
    B --> C[Khởi tạo:<br/>totalDiem = 0, totalTinChi = 0]
    C --> D[Lặp qua từng điểm]
    D --> E{loaiDiem == 'tongket'?}
    E -->|Không| D
    E -->|Có| F[totalDiem += diemSo * tinChi]
    F --> G[totalTinChi += tinChi]
    G --> D
    D --> H{totalTinChi == 0?}
    H -->|Có| I[Trả về 0.0]
    H -->|Không| J[GPA10 = totalDiem / totalTinChi]
    J --> K[Làm tròn 2 chữ số thập phân]
    K --> L[Trả về GPA10]
    I --> M[Kết thúc]
    L --> M
    
    style A fill:#e1f5ff
    style M fill:#c8e6c9
    style E fill:#fff9c4
    style H fill:#fff9c4
```

## Chi Tiết Các Bước

### 1. Tính Điểm Tổng Kết
- Công thức: quatrinh * 0.2 + giuaky * 0.3 + ketthuc * 0.5
- Làm tròn 2 chữ số thập phân
- Hỗ trợ các trường hợp thiếu điểm (null)

### 2. Chuyển Đổi Điểm Hệ 4
- Thang điểm chuyển đổi:
  - 9.0 - 10.0 → 4.0
  - 8.0 - 8.9 → 3.5
  - 7.0 - 7.9 → 3.0
  - 6.5 - 6.9 → 2.5
  - 5.5 - 6.4 → 2.0
  - 5.0 - 5.4 → 1.5
  - 4.0 - 4.9 → 1.0
  - < 4.0 → 0.0

### 3. Tính GPA Tổng
- Chỉ tính từ điểm tổng kết (loaiDiem = 'tongket')
- Công thức: Tổng (điểm * tín chỉ) / Tổng tín chỉ
- Làm tròn 2 chữ số thập phân

### 4. Tự Động Tính Điểm Tổng Kết
- Khi môn học có đủ 3 loại điểm, tự động tính và lưu điểm tổng kết
- Nếu đã có điểm tổng kết, cập nhật lại
- Nếu chưa có, tạo mới

