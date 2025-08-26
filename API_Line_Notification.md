# API Line Notification

## 1. Thông tin tổng quan

- **Tên hệ thống:** アクリート
- **Chức năng:** API Line Notification
- **Nội dung:** Detail Design
- **Ngày tạo:** 2025-05-14

---

## 2. Giải thích chức năng

API để gửi thông báo LINE đến người dùng cuối thông qua việc đăng ký SQS.

---

## 3. Xử lý tổng quan

API này nhận request từ hệ thống SMS và đăng ký job để gửi thông báo LINE. Công việc chính bao gồm xác thực số điện thoại, mã công ty, template ID và các tham số truyền vào.

### 3.1. Chức năng: Validate Param và đăng ký job
- **SUMMARY:** Nhận request từ hệ thống SMS và đăng ký job để gửi thông báo LINE.

### 3.2. Phương thức và Đường dẫn
- **METHOD:** `POST`
- **URL:** `/v1/line/notification-messages`
- **Giới hạn request:** Check `job_interval_seconds` trong `m_line_channel_config` xem có đang vượt quá không. Nếu vượt quá sẽ trả về 429 cùng message tương ứng.

### 3.3. Request Header

| No | Method | Value | Required | Description |
|----|--------|-------|----------|-------------|
| 1  | Accept | Content-Type: application/json | Yes | Định dạng nội dung gửi đi |

### 3.4. Request Parameters

| No | Name | Data type | Required | Description |
|----|------|-----------|----------|-------------|
| 1  | phoneNumber | String | YES | Số điện thoại người nhận |
| 2  | lineChannelId | String | YES | Mã công ty yêu cầu gửi |
| 3  | templateKey | String | YES | Key của template sử dụng |
| 4  | body | Object | No |  |
| 5  | requestTimeout | Number | No | Khoảng thời gian job kiểm tra webhook cho công ty (tính bằng giây), nếu quá thời gian chờ sẽ callback lại sang hệ thống SMS. Min: 30s, Max: 24h, Default: 5 phút = 300s |

### 3.5. Validations

| Tham số | Điều kiện | Mô tả xử lý (tham khảo) |
|---------|----------|------------------------|
| phoneNumber | Thuộc Nhật Bản hoặc Thái Lan hoặc Đài Loan | Sử dụng regex để kiểm tra định dạng số điện thoại và mã quốc gia |
| lineChannelId | Phải tồn tại trong hệ thống | Kiểm tra bằng query DB: `SELECT COUNT(1) FROM m_line_channel_config WHERE line_channel_id = :lineChannelId and disable_flag = 0` |
| templateKey | Phải tồn tại trong hệ thống và thuộc về lineChannelId | Kiểm tra bằng query DB: `SELECT COUNT(1) FROM m_company_line_template WHERE template_key = :templateKey AND line_channel_id = :lineChannelId` |
| body | Thỏa mãn mục 5.1 | Tham khảo mục 5.1 |

#### 3.5.1. Mẫu params và xử lý validate (mẫu, không cố định)

```json
"body": {
  "emphasizedItem": {
    "itemKey": "date_002_ja",
    "content": "Saturday, August 10, 2024"
  },
  "items": [
    { "itemKey": "time_range_001_ja", "content": "A.M." },
    { "itemKey": "number_001_ja", "content": "1234567" },
    { "itemKey": "price_001_ja", "content": "120 USD" },
    { "itemKey": "name_010_ja", "content": "Frozen Soup Set" }
  ],
  "buttons": [
    { "buttonKey": "check_delivery_status_ja", "url": "https://example.com/CheckDeliveryStatus/" },
    { "buttonKey": "Check_Contact", "url": "https://example.com/ContactUs/" }
  ]
}
```

#### 3.5.2. Quy tắc validate chi tiết

| Mô tả | Trường | Rule | Required | Ghi chú | Message (tham khảo common) |
|-------|--------|------|----------|--------|----------------------------|
| body | Không bắt buộc, nhưng nếu có phải validate các phần bên dưới | | X | Nếu không có thì vẫn hợp lệ | |
| emphasizedItem | Nếu có thì phải có itemKey và content không rỗng hoặc null | | X | Null thì hợp lệ | |
| emphasizedItem.itemKey | String not Empty or Null | | nếu có emphasizedItem | Trả về message lỗi tương ứng | Required: "必須項目 body.emphasizedItem.itemKey が入力されていません。" |
| emphasizedItem.content | String not Empty or Null | | nếu có emphasizedItem | Trả về message lỗi tương ứng | Required: "必須項目 body.emphasizedItem.content が入力されていません。"<br>Max length: "body.emphasizedItem.content は1文字から15文字の間で入力してください。" |
| items | Nếu có thì là mảng hoặc null hoặc rỗng | | X | | |
| items[i].itemKey | String not Empty or Null | | nếu item tồn tại | Trả về message lỗi tương ứng | Required: "必須項目 body.items[{i}].itemKey が入力されていません。" |
| items[i].content | String not Empty or Null | | nếu item tồn tại | Trả về message lỗi tương ứng | Required: "必須項目 body.items[{i}].content が入力されていません。"<br>Max length: "body.items[{i}].content は1文字から300文字の間で入力してください。" |
| buttons | Nếu có thì là mảng hoặc null hoặc rỗng | | X | | |
| buttons[i].url | Bắt đầu bằng http://, https://, hoặc mailto:,... | | nếu buttons tồn tại | Trả về message lỗi tương ứng | Required: "必須項目 buttons[{i}].buttonKey が入力されていません。" |
| buttons[i].buttonKey | String not Empty or Null | | nếu buttons tồn tại | Trả về message lỗi tương ứng | Required: "必須項目 buttons[{i}].url が入力されていません。"<br>Url Invalid: "buttons[{i}].url の入力形式が正しくありません。"<br>Max length: "buttons[{i}].url は1文字から1000文字の間で入力してください。" |

## 4. Quy trình xử lý nghiệp vụ

### Xử lý chung:
- Áp dụng memCache cho các truy vấn thường xuyên
- Sử dụng MemCache với thư viện Caffeine để cache dữ liệu truy cập nhiều (ví dụ: thông tin channel, template, ...).
- Thiết lập TTL (Time To Live) cho cache là 1 giờ (1h).
- Giúp giảm tải DB, tăng hiệu năng hệ thống. 

### 4.1. Xử lý Interceptor Layer

#### Giới hạn request (Rate Limiting)
- Áp dụng giới hạn request cho từng channelId dựa trên cấu hình `request_limit_second`.
- Cấu hình Interceptor cho WebMvcConfigurer để kiểm tra số lượng request trong khoảng thời gian cấu hình.
- Lấy giá trị giới hạn từ trường `m_line_channel_config.request_limit_second`.
- Nếu vượt quá giới hạn, trả về HTTP 429 Too Many Requests.

### 4.2. Xử lý Service Layer
#### Bước 1: Validate dữ liệu đầu vào

- Kiểm tra các trường bắt buộc: `phoneNumber`, `lineChannelId`, `templateKey`, `body`.
- Kiểm tra định dạng số điện thoại (chỉ cho phép các đầu số hợp lệ).
- Kiểm tra cấu trúc `body` theo rule của từng template (dùng MappingRule).
- Nếu có lỗi, trả về HTTP 400 với chi tiết lỗi từng trường.

#### Bước 2: Kiểm tra tồn tại & trạng thái của channel/template

- Kiểm tra `lineChannelId` có tồn tại và đang bật (disableFlag = false).
- Kiểm tra `templateKey` có tồn tại.
- Kiểm tra cặp `lineChannelId` + `templateKey` có mapping với nhau và đang bật.
- Nếu không hợp lệ, trả về HTTP 400 với thông báo lỗi tương ứng.

#### Bước 3: Lưu thông tin gửi vào DB

- Tạo entity `TLineMsgRequest` với các trường:
  - `lineChannelId`
  - `templateKey`
  - `phoneNumber` (hash SHA256)
  - `body` (serialize JSON)
  - `requestStatus = 0` (success)
  - `jobIntervalSeconds` (timeout)
- Lưu vào DB, lấy ra `lineMsgRequestId` (UUID).

#### Bước 4: Đẩy message vào SQS

- Gửi message lên SQS queue (FIFO), sử dụng:
  - `queueUrl` lấy từ config
  - `payload` là `lineMsgRequestId`
  - `messageGroupId` là `lineChannelId`
- Gửi bất đồng bộ, log lại kết quả gửi.

#### Bước 5: Trả về kết quả

- Nếu thành công: HTTP 202, body:
  ```json
  {
    "code": 202,
    "msg": "Accepted",
    "data": {
      "line_msg_request_id": "3003d327-8ecc-4475-a04c-126b612f89d7"
    }
  }
  ```
- Nếu lỗi validate: HTTP 400, body:
  ```json
  {
    "code": 400,
    "msg": "入力内容に誤りがあります。入力内容をご確認ください。",
    "data": {
      "phoneNumber": "電話番号の形式が正しくありません（日本、タイ、台湾の番号のみ対応。"
    }
  }
  ```
- Nếu lỗi Too many request: HTTP 429, body:
  ```json
  {
    "code": 429,
    "msg": "リクエストが集中しています。しばらく時間をおいてから再度お試しください。",
    "data": null
  }
  ```
- Nếu lỗi hệ thống: HTTP 500, body:
  ```json
  {
    "code": 500,
    "msg": "サーバー内部エラーが発生しました。現在リクエストを処理できません。しばらくしてから再度お試しください。",
    "data": "java.lang.Exception: ..."
  }
  ```

---

## 5. Luồng lỗi & xử lý ngoại lệ

- Nếu lỗi validate input: lưu bản ghi vào DB với `requestStatus = 1`, `errorMessage` là chi tiết lỗi.
- Nếu lỗi khi gửi SQS: log lỗi, không rollback DB.
- Nếu lỗi hệ thống: lưu bản ghi lỗi vào DB, trả về HTTP 500.

---

## 6. Ghi chú triển khai

- Sử dụng cache (memCacheService) để giảm truy vấn DB khi kiểm tra tồn tại channel/template.
- Validate body sử dụng rule động (MappingRuleConfig).
- Hash số điện thoại trước khi lưu DB.
- Gửi SQS bất đồng bộ, không chặn luồng chính.
- Log chi tiết thời gian từng bước để phục vụ monitoring.
