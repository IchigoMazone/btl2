package org.example.run;

import org.example.service.NotificationService;

public class Main1 {
    public static void main(String[] args) {
        // Test tạo và lưu 1 notification mới vào đầu danh sách trong XML
        NotificationService.createNotification(
                "B001",               // bookingId
                "R001",               // requestId
                "johndoe",            // userName
                "Yêu cầu đặt phòng đã được xử lý.", // content
                "Đã xử lý"            // status
        );

        NotificationService.updateStatusByUserAndBookingId("johndoe", "B001", "Đã đọc");
        NotificationService.updateStatusByUserAndRequestId("johndoe", "R002", "Đã gửi");
        System.out.println("Thông báo đã được lưu vào notifications.xml");
    }
}
