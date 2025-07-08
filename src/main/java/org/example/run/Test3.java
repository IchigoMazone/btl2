package org.example.run;

import org.example.entity.Person;
import org.example.service.BookingService;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Test3 {

    private static final String BOOKINGS_FILE = "bookings.xml";
    private static final String ROOMS_FILE = "rooms.xml";

    public static void main(String[] args) {

        // 1. Tạo danh sách người lưu trú (persons)
        Person p1 = new Person("Nguyen Van A", "123456789");
        Person p2 = new Person("Tran Thi B", "987654321");

        // 2. Tạo booking mới
        BookingService.createBooking(
                BOOKINGS_FILE,
                "BK20250707",  // Mã booking
                "REQ12346",    // Mã yêu cầu liên quan
                "user1",       // Username
                "Nguyen Van A",
                "user1@example.com",
                "0123456789",
                "R001",        // Mã phòng
                LocalDateTime.of(2025, 7, 7, 14, 0),
                LocalDateTime.of(2025, 7, 15, 12, 0),
                1500.0,
                Arrays.asList(p1, p2)
        );

        // 3. Cập nhật trạng thái booking
        BookingService.updateBookingStatus(BOOKINGS_FILE, "BK20250707", "Check-in");

        // 4. Liệt kê khách đang lưu trú (nếu thời gian hiện tại phù hợp)
        LocalDateTime now = LocalDateTime.now();
        System.out.println("\n=== Danh sách khách đang lưu trú ===");
        BookingService.listCurrentGuests(now, BOOKINGS_FILE);

        // 5. Kiểm tra trạng thái phòng
        System.out.println("\n=== Kiểm tra trạng thái phòng ===");
        BookingService.checkBookingStatus(BOOKINGS_FILE, ROOMS_FILE);
    }
}

