# Sơ Đồ Luồng Hoạt Động - DBUtil

## Mô tả
Utility class để quản lý kết nối database. Cung cấp method getConnection() để lấy Connection đến MySQL.

## Sơ Đồ Luồng - Phương Thức getConnection

```mermaid
flowchart TD
    A[Bắt đầu: getConnection] --> B[Load MySQL Driver<br/>Class.forName 'com.mysql.cj.jdbc.Driver']
    B --> C{Driver tìm thấy?}
    C -->|Không| D[Ném SQLException:<br/>MySQL Driver không tìm thấy]
    C -->|Có| E[Tạo Connection với:<br/>DB_URL, DB_USER, DB_PASSWORD]
    E --> F{Kết nối thành công?}
    F -->|Không| G[Ném SQLException]
    F -->|Có| H[Trả về Connection]
    H --> I[Kết thúc]
    D --> J[Kết thúc với lỗi]
    G --> J
    
    style A fill:#e1f5ff
    style I fill:#c8e6c9
    style J fill:#ffcdd2
    style C fill:#fff9c4
    style F fill:#fff9c4
```

## Sơ Đồ Luồng - Phương Thức closeConnection

```mermaid
flowchart TD
    A[Bắt đầu: closeConnection] --> B[Nhận Connection]
    B --> C{Connection != null?}
    C -->|Không| D[Kết thúc - không làm gì]
    C -->|Có| E[Gọi connection.close]
    E --> F{Có lỗi?}
    F -->|Có| G[In lỗi ra console]
    F -->|Không| H[Đóng connection thành công]
    D --> I[Kết thúc]
    G --> I
    H --> I
    
    style A fill:#e1f5ff
    style I fill:#c8e6c9
    style C fill:#fff9c4
    style F fill:#fff9c4
```

## Cấu Hình Database

```mermaid
flowchart TD
    A[DBUtil] --> B[DB_URL:<br/>jdbc:mysql://localhost:3306/<br/>quan_ly_hoc_tap?<br/>useSSL=false&<br/>serverTimezone=UTC&<br/>characterEncoding=UTF-8&<br/>useUnicode=true]
    A --> C[DB_USER: root]
    A --> D[DB_PASSWORD: 1111]
    B --> E[Kết nối đến MySQL]
    C --> E
    D --> E
    
    style A fill:#e1f5ff
    style E fill:#c8e6c9
```

## Chi Tiết Các Bước

### 1. Load Driver
- Sử dụng `Class.forName()` để load MySQL JDBC Driver
- Driver phải có trong classpath (thường qua Maven dependency)

### 2. Tạo Connection
- Sử dụng `DriverManager.getConnection()` với:
  - URL: jdbc:mysql://localhost:3306/quan_ly_hoc_tap
  - User: root
  - Password: 1111
- Các tham số URL:
  - useSSL=false: Tắt SSL
  - serverTimezone=UTC: Múi giờ UTC
  - characterEncoding=UTF-8: Encoding UTF-8
  - useUnicode=true: Hỗ trợ Unicode

### 3. Xử Lý Lỗi
- Nếu không tìm thấy driver, ném SQLException
- Nếu kết nối thất bại, ném SQLException
- Các exception được propagate lên caller

### 4. Đóng Connection
- Helper method để đóng connection an toàn
- Kiểm tra null trước khi đóng
- Bắt và log lỗi nếu có

### 5. Lưu Ý
- Connection nên được đóng sau khi sử dụng
- Sử dụng try-with-resources để tự động đóng
- Thông tin kết nối nên được config từ file properties (hiện tại hardcode)

