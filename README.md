# PHẦN MỀM QUẢN LÝ ĐẶT PHÒNG KHÁCH SẠN

## 1. Mô tả bài toán

Phần mềm được thiết kế để hỗ trợ quy trình quản lý khách sạn, với các chức năng chính sau:

* Đăng ký và quản lý người dùng
* Tạo và xử lý đơn đặt phòng
* Kiểm tra trạng thái phòng (còn trống, đã đặt, đang sử dụng)
* Quản lý thông tin khách hàng

## Hệ thống có 2 vai trò người dùng chính:

* **Admin**:

    * Quản lý danh sách phòng
    * Quản lý tài khoản người dùng
    * Duyệt và hủy đơn đặt phòng
    * Cập nhật trạng thái phòng

* **Customer** (Khách hàng):

    * Tìm kiếm và đặt phòng
    * Xem và cập nhật thông tin cá nhân
    * Theo dõi lịch sử đặt phòng

## 2. Giả thuyết và Triển khai thực tế

### Giả thuyết hệ thống

* Dữ liệu được lưu dưới dạng các tệp `.xml` đóng vai trò như cơ sở dữ liệu phi quan hệ.
* Các tệp XML này nằm trên máy chủ trung tâm, nơi người dùng (Admin, Customer) sẽ truy cập và thao tác từ xa.

### Thực tế triển khai

* Để đơn giản hóa việc thử nghiệm và kiểm tra:

    * Các tệp `.xml` sẽ được lưu trữ cục bộ ngay trên máy người dùng.
    * Người dùng có thể đồng thời đóng vai cả Admin và Customer để kiểm tra các chức năng của cả hai vai trò trên một thiết bị duy nhất.

## 3. Thông tin tài khoản mặc định

* **Admin**

    * Tên đăng nhập: `admin`
    * Mật khẩu: `123456`

* **Customer**

    * Tên đăng nhập: `husky123`
    * Mật khẩu: `nhat1234`

## 4. Công nghệ sử dụng

### Ngôn ngữ lập trình: Java

### Phiên bản JDK: 21 trở lên (JDK 21+)

* Bảo đảm tính tương thích và hỗ trợ các tính năng mới trong Java 21 (LTS)

### Môi trường phát triển: Apache NetBeans

* Hỗ trợ tốt Java Maven project
* Hệ thống giao diện đồ họa (nếu có) có thể phát triển bằng Java Swing hoặc JavaFX
* Tích hợp dễ dàng với trình biên dịch JDK và công cụ quản lý thư viện (Maven)
