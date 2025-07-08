package org.example.run;

import org.example.service.BookingService;
import java.time.LocalDateTime;

public class Test5 {
    public static void main(String[] args) {
        String bookingPath = "bookings.xml";

        // Lấy thời điểm hiện tại
        LocalDateTime now = LocalDateTime.now();

        System.out.println("Danh sách khách đang lưu trú tại thời điểm: " + now);

        // Gọi hàm listCurrentGuests để in danh sách khách đang ở
        BookingService.listCurrentGuests(now, bookingPath);
    }
}
