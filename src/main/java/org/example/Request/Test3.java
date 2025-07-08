//package org.example.Request;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//public class Test3 {
//
//    private static final String BOOKINGS_FILE = "bookings.xml";
//    private static final String ROOMS_FILE = "rooms.xml";
//
//    public static void main(String[] args) {
//
//        // 1. Tạo danh sách người lưu trú (persons)
////        Person p1 = new Person("Nguyen Van A", "123456789");
////        Person p2 = new Person("Tran Thi B", "987654321");
////
////        // 2. Tạo booking mới
////        SetupBooking.createBooking(
////                BOOKINGS_FILE,
////                "BK20250707",
////                "REQ12346",
////                "user1",
////                "Nguyen Van A",
////                "user1@example.com",
////                "0123456789",
////                "R001",
////                LocalDateTime.of(2025, 7, 7, 14, 0),
////                LocalDateTime.of(2025, 7, 15, 12, 0),
////                1500.0,
////                Arrays.asList(p1, p2)
////        );
//
//        // 3. Cập nhật trạng thái booking
//        SetupBooking.updateBookingStatus(BOOKINGS_FILE, "BK20250707", "Check-in");
//
////        // 4. Liệt kê khách đang lưu trú (nên thấy BK20250708 nếu thời gian hiện tại phù hợp)
////        System.out.println("\n=== Danh sách khách đang lưu trú ===");
////        SetupBooking.listCurrentGuests(BOOKINGS_FILE);
////
////        // 5. Kiểm tra trạng thái hoạt động của các phòng
////        System.out.println("\n=== Kiểm tra trạng thái phòng ===");
//        SetupBooking.checkBookingStatus(BOOKINGS_FILE, ROOMS_FILE);
//    }
//}
