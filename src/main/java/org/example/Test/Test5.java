package org.example.Test;

import java.time.LocalDateTime;
import java.util.List;

public class Test5 {
    public static void main(String[] args) {
        String bookingPath = "bookings.xml";
        BookingUserFilterService service = new BookingUserFilterService(bookingPath);

        // Khoảng thời gian tìm kiếm
        LocalDateTime from = LocalDateTime.of(2025, 7, 5, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 7, 12, 23, 59);

        List<String> users = service.getUsernamesByCheckInBetween(from, to);

        if (users.isEmpty()) {
            System.out.println("❌ Không có người dùng nào check-in trong khoảng.");
        } else {
            System.out.println("✅ Người dùng check-in trong khoảng:");
            users.forEach(u -> System.out.println("- " + u));
        }
    }
}


