# Sơ Đồ Luồng Hoạt Động - CharacterEncodingFilter

## Mô tả
Filter để xử lý encoding UTF-8 cho request và response. Đảm bảo dữ liệu tiếng Việt được xử lý đúng.

## Sơ Đồ Luồng - Phương Thức doFilter

```mermaid
flowchart TD
    A[Bắt đầu: doFilter] --> B[Set encoding UTF-8 cho request<br/>request.setCharacterEncoding 'UTF-8']
    B --> C[Set encoding UTF-8 cho response<br/>response.setCharacterEncoding 'UTF-8']
    C --> D[Set content type với charset<br/>response.setContentType<br/>'text/html; charset=UTF-8']
    D --> E[Tiếp tục filter chain<br/>chain.doFilter request, response]
    E --> F[Kết thúc]
    
    style A fill:#e1f5ff
    style F fill:#c8e6c9
```

## Sơ Đồ Luồng - Xử Lý Encoding

```mermaid
flowchart TD
    A[Request đến server] --> B[CharacterEncodingFilter xử lý]
    B --> C[Set request encoding UTF-8]
    C --> D[Set response encoding UTF-8]
    D --> E[Set content type với charset]
    E --> F[Request được chuyển đến servlet]
    F --> G[Servlet đọc parameters<br/>với encoding UTF-8]
    G --> H[Servlet ghi response<br/>với encoding UTF-8]
    H --> I[Browser nhận response<br/>với charset UTF-8]
    I --> J[Hiển thị đúng tiếng Việt]
    
    style A fill:#e1f5ff
    style J fill:#c8e6c9
```

## Chi Tiết Các Bước

### 1. Set Request Encoding
- `request.setCharacterEncoding("UTF-8")`
- Phải được gọi trước khi đọc parameters
- Đảm bảo parameters được decode đúng UTF-8

### 2. Set Response Encoding
- `response.setCharacterEncoding("UTF-8")`
- Đảm bảo response được encode đúng UTF-8

### 3. Set Content Type
- `response.setContentType("text/html; charset=UTF-8")`
- Thông báo cho browser về encoding
- Browser sẽ hiển thị đúng tiếng Việt

### 4. Filter Chain
- Gọi `chain.doFilter()` để tiếp tục xử lý
- Filter này phải được thực thi trước các filter/servlet khác

### 5. WebFilter Annotation
- @WebFilter("/*") áp dụng cho tất cả requests
- Đảm bảo mọi request/response đều dùng UTF-8

### 6. Lưu Ý
- Phải set encoding trước khi đọc request parameters
- Phải set encoding trước khi ghi response
- Filter này nên được thực thi đầu tiên trong filter chain

