# Sơ Đồ Luồng Hoạt Động - Hệ Thống Quản Lý Học Tập

## Tổng Quan

Thư mục này chứa các sơ đồ luồng hoạt động chi tiết cho từng file trong dự án, được viết bằng tiếng Việt và sử dụng Mermaid để hiển thị.

## Cấu Trúc Thư Mục

```
flow-diagrams/
├── controller/          # Các Servlet xử lý request
├── dao/                # Data Access Object - tương tác với database
├── model/              # Các class model đại diện cho dữ liệu
├── service/            # Các service xử lý business logic
├── util/               # Các utility class
└── filter/             # Các filter xử lý request/response
```

## Controller (12 files)

### Xác Thực và Đăng Nhập
- **[RegisterServlet.md](controller/RegisterServlet.md)** - Xử lý đăng ký tài khoản
- **[LoginServlet.md](controller/LoginServlet.md)** - Xử lý đăng nhập
- **[LogoutServlet.md](controller/LogoutServlet.md)** - Xử lý đăng xuất

### Trang Chủ và Dashboard
- **[HomeServlet.md](controller/HomeServlet.md)** - Trang chủ
- **[DashboardServlet.md](controller/DashboardServlet.md)** - Trang dashboard sau đăng nhập

### Quản Lý Hồ Sơ
- **[ProfileServlet.md](controller/ProfileServlet.md)** - Quản lý hồ sơ cá nhân

### Quản Lý Lịch Học
- **[ScheduleServlet.md](controller/ScheduleServlet.md)** - CRUD lịch học
- **[ScheduleImportServlet.md](controller/ScheduleImportServlet.md)** - Import lịch học từ CSV

### Quản Lý Điểm Số
- **[GradeServlet.md](controller/GradeServlet.md)** - CRUD điểm số

### Quản Lý Tiến Độ
- **[ProgressServlet.md](controller/ProgressServlet.md)** - CRUD tiến độ học tập (tasks)

### Quản Lý Tài Liệu
- **[DocumentServlet.md](controller/DocumentServlet.md)** - Hiển thị và xóa tài liệu
- **[UploadDocumentServlet.md](controller/UploadDocumentServlet.md)** - Upload tài liệu
- **[DownloadDocumentServlet.md](controller/DownloadDocumentServlet.md)** - Download tài liệu

## DAO (6 files)

- **[UserDAO.md](dao/UserDAO.md)** - Tương tác với bảng users
- **[UserProfileDAO.md](dao/UserProfileDAO.md)** - Tương tác với bảng user_profiles
- **[ScheduleDAO.md](dao/ScheduleDAO.md)** - Tương tác với bảng schedules
- **[GradeDAO.md](dao/GradeDAO.md)** - Tương tác với bảng grades
- **[ProgressDAO.md](dao/ProgressDAO.md)** - Tương tác với bảng progress
- **[DocumentDAO.md](dao/DocumentDAO.md)** - Tương tác với bảng documents

## Model (6 files)

- **[User.md](model/User.md)** - Model đại diện cho user
- **[UserProfile.md](model/UserProfile.md)** - Model đại diện cho user profile
- **[Schedule.md](model/Schedule.md)** - Model đại diện cho lịch học
- **[Grade.md](model/Grade.md)** - Model đại diện cho điểm số
- **[Progress.md](model/Progress.md)** - Model đại diện cho tiến độ học tập
- **[Document.md](model/Document.md)** - Model đại diện cho tài liệu

## Service (1 file)

- **[GPAService.md](service/GPAService.md)** - Service tính toán GPA và điểm tổng kết

## Util (3 files)

- **[DBUtil.md](util/DBUtil.md)** - Utility quản lý kết nối database
- **[PasswordUtil.md](util/PasswordUtil.md)** - Utility mã hóa và kiểm tra mật khẩu
- **[FileUtil.md](util/FileUtil.md)** - Utility xử lý file upload

## Filter (2 files)

- **[AuthFilter.md](filter/AuthFilter.md)** - Filter bảo vệ các trang cần đăng nhập
- **[CharacterEncodingFilter.md](filter/CharacterEncodingFilter.md)** - Filter xử lý encoding UTF-8

## Cách Sử Dụng

1. Mở file markdown tương ứng với component bạn muốn tìm hiểu
2. Xem sơ đồ luồng bằng Mermaid (hỗ trợ trên GitHub, VS Code với extension Mermaid, hoặc các công cụ online)
3. Đọc phần "Chi Tiết Các Bước" để hiểu rõ hơn về logic

## Lưu Ý

- Tất cả sơ đồ được viết bằng tiếng Việt
- Sử dụng Mermaid syntax cho flowchart và class diagram
- Mỗi file bao gồm:
  - Mô tả tổng quan
  - Sơ đồ luồng cho từng phương thức quan trọng
  - Chi tiết các bước xử lý

## Tổng Số Files

- **Controller**: 12 files
- **DAO**: 6 files
- **Model**: 6 files
- **Service**: 1 file
- **Util**: 3 files
- **Filter**: 2 files
- **Tổng cộng**: 30 files

