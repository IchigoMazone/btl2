//package org.example.Test;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public class Test1 {
//    public static void main(String[] args) {
//        String filePath = "bookings.xml";
//
//        // 1. Tạo các đối tượng người
//        Person representative = new Person("John Doe", "123456789000");
//        Person companion1 = new Person("Nguyễn Văn B", "012345678912");
//        Person companion2 = new Person("Lê Thị C", "098765432198");
//        List<Person> companions = List.of(companion1, companion2);
//
//        // 2. Thông tin booking
//        String bookingId = "BK" + System.currentTimeMillis();
//        String requestId = "REQ123";
//        String userName = "johndoe";
//        String fullName = "John Doe";
//        String email = "john@example.com";
//        String phone = "0901234567";
//        String roomId = "R001";
//        LocalDateTime checkIn = LocalDateTime.of(2025, 7, 7, 12, 0);
//        LocalDateTime checkOut = LocalDateTime.of(2025, 7, 9, 12, 0);
//        double amount = 2500000;
//
//        // 3. Tạo booking
//        SetupBooking.createBooking(filePath, bookingId, requestId, userName, fullName, email, phone,
//                roomId, checkIn, checkOut, amount, representative, companions);
//
//        // 4. Cập nhật trạng thái booking
//        SetupBooking.updateBookingStatus(filePath, bookingId, "CheckIn");
//
//        // 5. Kiểm tra trạng thái hoạt động các phòng
//        SetupBooking.checkBookingStatus(filePath, "rooms.xml");
//
//        // 6. Liệt kê người đang lưu trú
//        SetupBooking.listCurrentGuests(filePath);
//    }
//}
