# Hệ Thống Quản Lý Học Tập (LMS)

Hệ thống web quản lý học tập cho sinh viên, sử dụng Java Servlet + JSP + MVC, chạy trên Tomcat 9, kết nối MySQL.

## Công nghệ sử dụng

- **Backend**: Java Servlet + JSP
- **Database**: MySQL 8+
- **Application Server**: Apache Tomcat 9+
- **Build Tool**: Apache Maven 3.6+
- **JDK**: JDK 23

## Cấu trúc project

```
lms-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── lms/
│   │   │           ├── controller/    # Các Servlet
│   │   │           ├── model/        # Các POJO
│   │   │           ├── dao/          # Các DAO
│   │   │           ├── util/         # Utility classes
│   │   │           └── filter/       # Các Filter
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml
│   │       │   └── views/            # Các file JSP
│   │       ├── assets/               # CSS, JS, images
│   │       └── uploads/              # Thư mục lưu file upload
│   └── test/
└── pom.xml
```

## Hướng dẫn cài đặt và chạy

### 1. Cài đặt Database

1. Mở MySQL và chạy file `database_setup.sql`:
```sql
mysql -u root -p < database_setup.sql
```

Hoặc copy nội dung file vào MySQL Workbench và chạy.

2. Cập nhật thông tin kết nối database trong `src/main/java/com/lms/util/DBUtil.java`:
```java
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password"; // Điền password MySQL của bạn
```

### 2. Build project

```bash
cd lms-project
mvn clean package
```

File WAR sẽ được tạo trong thư mục `target/lms-project.war`

### 3. Deploy lên Tomcat

**Cách 1: Copy WAR file**
```bash
copy target\lms-project.war ..\apache-tomcat-9.0.113\webapps\
```

**Cách 2: Sử dụng Tomcat Manager** (nếu đã cấu hình)

### 4. Start Tomcat

```bash
cd ..\apache-tomcat-9.0.113\bin
startup.bat
```

### 5. Truy cập ứng dụng

Mở trình duyệt và truy cập:
```
http://localhost:8080/lms-project/
```

## Các chức năng đã hoàn thành

### ✅ Phần 0: Setup môi trường
- Tạo Maven project với cấu trúc chuẩn
- Cấu hình pom.xml với đầy đủ dependencies
- Cấu hình web.xml
- HomeServlet và home.jsp

### ✅ Phần 1: Database + JDBC
- Database `quan_ly_hoc_tap`
- Bảng `users`, `user_profiles`, `schedules`, `progress`, `grades`, `gpa`, `documents`
- DBUtil để quản lý kết nối database
- User model và UserDAO

### ✅ Phần 2: Đăng ký/Đăng nhập/Đăng xuất
- PasswordUtil với BCrypt để hash password
- RegisterServlet, LoginServlet, LogoutServlet
- DashboardServlet với menu điều hướng
- JSP: login.jsp, register.jsp, dashboard.jsp

### ✅ Phần 3: AuthFilter
- Filter bảo vệ các trang cần đăng nhập
- Cho phép truy cập public paths (login, register, assets)

### ✅ Phần 4: Hồ sơ cá nhân
- Bảng `user_profiles`
- UserProfile model và UserProfileDAO
- ProfileServlet với createOrUpdate
- profile.jsp

### ✅ Phần 5: Lịch học (CRUD + Import CSV)
- Bảng `schedules`
- Schedule model và ScheduleDAO với checkConflict
- ScheduleServlet xử lý CRUD
- ScheduleImportServlet xử lý import CSV
- schedule.jsp với form thêm/sửa và bảng danh sách

### ✅ Phần 6: Tiến độ học tập
- Bảng `progress`
- Progress model và ProgressDAO
- ProgressServlet với toggle status
- progress.jsp hiển thị tasks chưa xong/đã xong

### ✅ Phần 7: Điểm số + GPA
- Bảng `grades` và `gpa`
- Grade model và GradeDAO
- GPAService tính GPA học kỳ và GPA tổng
- GradeServlet tự động cập nhật GPA
- grades.jsp với filter theo học kỳ

### ✅ Phần 8: Tài liệu học tập
- Bảng `documents`
- Document model và DocumentDAO
- FileUtil xử lý file upload/download
- DocumentServlet, UploadDocumentServlet, DownloadDocumentServlet
- documents.jsp với upload form và danh sách tài liệu

### ✅ Phần 9: Hoàn thiện UI
- Dashboard với menu điều hướng đẹp mắt
- CSS chung (style.css) với responsive design
- JavaScript validation (validation.js)
- JavaScript common utilities (common.js)
- UI đồng nhất trên tất cả các trang

## Lưu ý

- Đảm bảo MySQL đang chạy trước khi start Tomcat
- Kiểm tra port 8080 không bị chiếm bởi ứng dụng khác
- Cập nhật password MySQL trong DBUtil.java

## Tác giả

Đồ án Lập trình Web - Hệ Thống Quản Lý Học Tập

