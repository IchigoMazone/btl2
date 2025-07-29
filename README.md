Phần mềm quản lý đặt phòng khách sạn

1. Mô tả bài toán

Phần mềm được thiết kế để quản lý quá trình đặt phòng khách sạn, bao gồm các chức năng như: đăng 
ký người dùng, tạo đơn đặt phòng,kiểm tra trạng thái phòng,và quản lý thông tin khách hàng. Hệ
thống có phân quyền người dùng với hai vai trò chính:

+ Admin: Quản lý phòng, người dùng, đơn đặt phòng,...
+ Customer: Đặt phòng và theo dõi thông tin cá nhân,...

 2. Giả thuyết và triển khai thực tế

Phần mềm được xây dựng dựa trên giả thuyết rằng:

+ Dữ liệu được lưu trữ dưới dạng các tệp .xml đóng vai trò như cơ sở dữ liệu.
+ Các tệp này nằm trên máy chủ trung tâm, nơi Admin và Customer kết nối tới để thực hiện thao tác.

Tuy nhiên, trong quá trình triển khai thực tế, để đơn giản hóa mô hình, các tệp .xml sẽ được lưu trữ
cục bộ ngay trên máy người dùng. Đồng thời, người dùng trong hệ thống có thể đảm nhận đồng thời vai
trò của cả Admin và Customer, nhằm mục đích kiểm thử và mô phỏng hệ thống một cách thuận tiện trong
môi trường đơn máy.

3. Mô tả phần mềm

Phần mềm được chia thành phần chính :

+ Giao diện liên kết: giao diện đăng nhâp, quên mật khẩu, tạo tài khoản,....
+ Giao diện Admin (AdminContainerView): Là một bao đóng các giao được liên kết với nhau và có thể di
chuyển qua lại linh hoạt (giao diên tìm kiếm, đặt phòng, quản lý phòng, thống kê,...)
+ Giao diện Customer (CustomerContainerView) : tương tự giao diện này cũng được liên kết với nhau
tạo ra một bao đóng (giao diện tìm kiếm, thông báo, lịch sử và giới thiệu)

3.1. Giao diện liên kết 
Bao gồm : đăng nhập, quên tài khoản (Kiểm tra tài khoản và tạo lại mật khẩu), ngược lại là giao diện 
Tạo tài khoản và giao diện trung gian thông báo Sửa hoặc tạo thành công. 

Sơ đồ minh họa :

      |--------------------------------------------------------------------|
      |                                                                    |
      |                                                                    |
      v                                                                    |
[  Đăng nhập  ]<---->[Kiểm tra tài khoản]<---->[Tạo mật khẩu mới]<---->[Thông báo] 
      ^                                                                    ^
      |                                                                    |
      v                                                                    |
[Tạo tài khoản]------------------------------------------------------------|


3.1.1. Đăng nhập : Đăng nhập để vào giao diện Admin, Customer chung một giao diện có quy tắc để không thể 
trùng lặp
3.1.2. Tạo tài khoản : Người dùng nhập thông tin để tạo tài khoản sử dụng trong phần mềm
3.1.3. Quên tài khoản : Bao gồm 2 giao diện là Kiểm tra tài khoản và Tạo mật khẩu
+ Kiểm tra tài khoản: Để chắc chắn tài khoản từng tồn tại trên hệ thống
+ Tạo tài khoản mới: Cập nhật tài khoản mới khi quên hay có nhu cầu

3.2. Giao diện Admin
Bao gồm : Tìm kiếm, đặt phòng, phòng nghỉ, trang chủ, thống kê, khách hàng, yêu cầu
3.2.1. Tìm kiếm: Chức năng tìm kiếm theo Check-in và Check-out với các loại phòng khác nhau với điều khoản 
riêng vì đây là giao diện Admin nên nó sẽ là đặt phòng trực tiếp vì vậy sẽ có điều kiện như sau:

[Tìm kiếm] ----> [Tạo Booking] : Sơ đồ 

if ( Check-in - now <= 15p ) -> Trạng thái: Check-in
ìf ( Check-in - now > 15p )  -> Trạng thái: Đã đặt

Khách hàng có thể linh động về thời gian 
3.2.2. Đặt phòng: Chức năng quản lý chuyển trạng thái các booking 

Đã đặt     ---->   Check-in
Check-in   ---->   Check-out : [Thanh toán]
Check-in   ---->   Chờ thanh toán  ---->   Check-out : [Thanh toán]

Trạng thái: Đã đặt     ----> if (Check-in - now <= 60p ) -> Có thể : Check-in nhận phòng sớm
                       ----> if (Check-in - now > 60p )  -> Không được Check-in
                       ----> if (now - Check-in >= 60p ) -> No-show : Đã đăt ----> Vắng mặt

Trạng thái: Check-in   ----> Có thể thanh toán sớm ----> Check-out
                       ----> if (now >= Check-out) ----> Chờ thanh toán : Trả phòng chưa thanh toán
                       (Trống phòng để các khách có thể đến ngay sau đó)

3.2.3. Phòng nghỉ      ----> Thêm, sửa, xóa, tìm kiếm phòng theo thông tin
3.2.4. Trang chủ       ----> Thống kê : Số phòng trống, hoạt động, số khách lưu trú, số check-in, out
                       hôm nay,...

3.2.5. Thống kê        ----> Thống kê : Lượt khách tháng náy, tổng lượt khách, tổng doanh thu, ngày có
                       lượt khách cao nhất

3.2.6. Yêu cầu         ----> Duyệt các yêu cầu đặt, hủy phòng của khách

3.3. Giao diện Customer
Bao gồm : Tìm kiếm, giới thiệu, lịch sử, và thông báo
3.3.1. Tìm kiếm: tương tự của Admin tìm kiếm theo Check-in, out và loại phòng

[Tìm kiếm] ----> [Tạo yêu cầu] ----> [Yêu cầu (Admin)] : Sơ đồ

if ( Check-in - now >= 240p ) ----> Tạo Request : Gửi yêu cầu
if ( Check-in - now < 24p )   ----> Không thể tạo Request

3.3.2. Giới thiệu: Trang giới thiệu các tiện ích của Khách sạn
3.3.3. Lịch sử: Tổng hợp tất cả lịch sử booking
3.3.4. Thông báo: Gửi về thông báo liên quan ví dụ như "Duyệt đặt phòng", "Bị hủy yêu cầu",...

4. Liên kết giữa 3 database
+ Xử lý yêu cầu   : requests.xml
+ Xử lý thống báo : notifications.xml
+ Xử lý đặt phòng : bookings.xml

                                                (Tiếp : 1)
[Customer] ------------> [Tạo yêu cầu] ------------------------------->
             Tìm kiếm                      Request : Gửi yêu cầu
                                      Notification : Yêu cầu duyệt



                                                 Request : Đã được duyệt
       (Tiếp : 1)                           Notification : Đã được duyệt
----------------------> [Admin] -----> [Duyệt] ------------------------>
                                |                    (Tiếp : 2)
                                |
                                |
                                |                    Request : Đã bị hủy
                                |               Notification : Đã bị hủy
                                |----> [ Hủy ] ------------------------>
                                                     (Tiếp : 3)
                                                     


     (Tiếp : 3)
      
[Admin] ---> [ Hủy ] : TH1 : |----->  Admin duyệt hủy vì một lý do nào đó 
                             |
                             |
                       TH2 : |-----> Hệ thống duyệt hủy tự động 









                   

