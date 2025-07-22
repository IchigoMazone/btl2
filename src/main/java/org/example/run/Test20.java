package org.example.run;

import org.example.service.BookingService;

import java.time.LocalDateTime;

public class Test20 {
    public static void main(String[] args) {
        // Đường dẫn đến file XML chứa dữ liệu Booking
        String filePath = "bookings.xml"; // Điều chỉnh đường dẫn nếu cần

        // Thời điểm hiện tại
        LocalDateTime currentTime = LocalDateTime.now();

        // Gọi hàm hiển thị danh sách khách đang lưu trú
        BookingService.listCurrentGuests(currentTime, filePath);
    }
}
