# Sơ Đồ Luồng Hoạt Động - UserProfile Model

## Mô tả
Model class đại diện cho bảng user_profiles. Lưu trữ thông tin hồ sơ cá nhân của sinh viên.

## Cấu Trúc Dữ Liệu

```mermaid
classDiagram
    class UserProfile {
        -int id
        -int userId
        -String hoTen
        -String chuyenNganh
        -String namHoc
        -Timestamp updatedAt
        +UserProfile()
        +UserProfile(id, userId, hoTen, chuyenNganh, namHoc, updatedAt)
        +UserProfile(userId, hoTen, chuyenNganh, namHoc)
        +getId() int
        +setId(int id)
        +getUserId() int
        +setUserId(int userId)
        +getHoTen() String
        +setHoTen(String hoTen)
        +getChuyenNganh() String
        +setChuyenNganh(String chuyenNganh)
        +getNamHoc() String
        +setNamHoc(String namHoc)
        +getUpdatedAt() Timestamp
        +setUpdatedAt(Timestamp time)
        +toString() String
    }
```

## Sơ Đồ Luồng - Khởi Tạo Object

```mermaid
flowchart TD
    A[Tạo UserProfile object] --> B{Constructor nào?}
    B -->|UserProfile| C[Tạo object rỗng<br/>Tất cả fields = null/0]
    B -->|UserProfile id, userId, ...| D[Tạo object với đầy đủ thông tin<br/>bao gồm id và updatedAt]
    B -->|UserProfile userId, hoTen, ...| E[Tạo object khi tạo mới<br/>chưa có id và updatedAt]
    C --> F[Object sẵn sàng sử dụng]
    D --> F
    E --> F
    
    style A fill:#e1f5ff
    style F fill:#c8e6c9
    style B fill:#fff9c4
```

## Sơ Đồ Luồng - Mối Quan Hệ Với User

```mermaid
flowchart TD
    A[User có UserProfile] --> B[1 User : 1 UserProfile]
    B --> C[UserProfile.userId tham chiếu đến User.id]
    C --> D[Khi tạo UserProfile mới<br/>phải có userId hợp lệ]
    D --> E[Khi cập nhật UserProfile<br/>có thể đồng bộ hoTen sang User]
    
    style A fill:#e1f5ff
    style E fill:#c8e6c9
```

## Chi Tiết Các Trường

### 1. id (int)
- ID tự động tăng của profile
- Được tạo bởi database khi insert

### 2. userId (int)
- ID của user sở hữu profile này
- Foreign key tham chiếu đến users.id
- Mỗi user chỉ có một profile

### 3. hoTen (String)
- Họ và tên của sinh viên
- Có thể null
- Có thể đồng bộ với User.hoTen

### 4. chuyenNganh (String)
- Chuyên ngành học của sinh viên
- Có thể null

### 5. namHoc (String)
- Năm học hiện tại của sinh viên
- Có thể null

### 6. updatedAt (Timestamp)
- Thời gian cập nhật profile lần cuối
- Được set tự động bởi database khi update

## Các Constructor

1. **UserProfile()**: Constructor mặc định, tạo object rỗng
2. **UserProfile(id, userId, hoTen, chuyenNganh, namHoc, updatedAt)**: Constructor đầy đủ, dùng khi đọc từ database
3. **UserProfile(userId, hoTen, chuyenNganh, namHoc)**: Constructor khi tạo profile mới, chưa có id và updatedAt

