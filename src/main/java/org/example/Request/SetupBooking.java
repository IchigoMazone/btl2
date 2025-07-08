//package org.example.Request;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SetupBooking {
//
//    // Tạo booking mới
//    public static void createBooking(String filePath,
//                                     String bookingId,
//                                     String requestId,
//                                     String userName,
//                                     String fullName,
//                                     String email,
//                                     String phone,
//                                     String roomId,
//                                     LocalDateTime checkIn,
//                                     LocalDateTime checkOut,
//                                     double amount,
//                                     List<Person> persons) {
//
//        BookingXML bookingXML = XMLUtil.readFromFile(filePath, BookingXML.class);
//        if (bookingXML == null) {
//            bookingXML = new BookingXML();
//        }
//
//        List<Booking> bookings = bookingXML.getBookings();
//        if (bookings == null) {
//            bookings = new ArrayList<>();
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//
//        // Tạo danh sách lịch sử
//        List<HistoryEntry> historyList = new ArrayList<>();
//        historyList.add(new HistoryEntry(now, "Gửi yêu cầu"));
//        historyList.add(new HistoryEntry(now, "Duyệt yêu cầu"));
//        historyList.add(new HistoryEntry(now, "Check-in"));
//        historyList.add(new HistoryEntry(now, "Check-out"));
//
//        // Tạo đối tượng Booking mới
//        Booking newBooking = new Booking(
//                bookingId,
//                requestId,
//                userName,
//                fullName,
//                email,
//                phone,
//                roomId,
//                checkIn,
//                checkOut,
//                amount,
//                now,
//                "Đã trả phòng",
//                persons,
//                historyList
//        );
//
//        bookings.add(0, newBooking); // Thêm booking mới lên đầu danh sách
//        bookingXML.setBookings(bookings);
//        XMLUtil.writeToFile(filePath, bookingXML);
//
//        System.out.println("✅ Tạo booking thành công: " + bookingId);
//    }
//
//    // Cập nhật trạng thái booking theo bookingId
//    public static void updateBookingStatus(String filePath, String bookingId, String newStatus) {
//        BookingXML bookingXML = XMLUtil.readFromFile(filePath, BookingXML.class);
//        if (bookingXML == null || bookingXML.getBookings() == null) {
//            System.out.println("❌ Không có dữ liệu booking.");
//            return;
//        }
//
//        boolean updated = false;
//        for (Booking booking : bookingXML.getBookings()) {
//            if (booking.getBookingId().equals(bookingId)) {
//                booking.setStatus(newStatus);
//                booking.addBookingHistory(newStatus, LocalDateTime.now());
//                updated = true;
//                break;
//            }
//        }
//
//        if (updated) {
//            XMLUtil.writeToFile(filePath, bookingXML);
//            System.out.println("✅ Cập nhật trạng thái thành công: " + bookingId);
//        } else {
//            System.out.println("❌ Không tìm thấy booking với ID: " + bookingId);
//        }
//    }
//
//    // Liệt kê người đang lưu trú tại khách sạn
//    public static void listCurrentGuests(String filePath) {
//        BookingXML bookingXML = XMLUtil.readFromFile(filePath, BookingXML.class);
//        if (bookingXML == null || bookingXML.getBookings() == null || bookingXML.getBookings().isEmpty()) {
//            System.out.println("❌ Không có dữ liệu booking.");
//            return;
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//        List<Booking> activeBookings = new ArrayList<>();
//
//        for (Booking booking : bookingXML.getBookings()) {
//            if ("Check-in".equalsIgnoreCase(booking.getStatus())
//                    && !now.isBefore(booking.getCheckIn())
//                    && !now.isAfter(booking.getCheckOut())) {
//                activeBookings.add(booking);
//            }
//        }
//
//        if (activeBookings.isEmpty()) {
//            System.out.println("❌ Không có ai đang ở khách sạn.");
//            return;
//        }
//
//        System.out.println("🏨 Danh sách người đang lưu trú:");
//        for (Booking booking : activeBookings) {
//            for (Person person : booking.getPersons()) {
//                System.out.println("👤 " + person.getName() +
//                        " (CCCD: " + person.getCccd() + ") 🛏️ Phòng: " + booking.getRoomId() +
//                        " 📄 Booking ID: " + booking.getBookingId());
//            }
//        }
//    }
//
//    // Kiểm tra trạng thái hoạt động của các phòng
//    public static void checkBookingStatus(String bookingFilePath, String roomFilePath) {
//        BookingXML bookingXML = XMLUtil.readFromFile(bookingFilePath, BookingXML.class);
//        RoomXML roomXML = XMLUtil.readFromFile(roomFilePath, RoomXML.class);
//
//        if (roomXML == null || roomXML.getRooms() == null || roomXML.getRooms().isEmpty()) {
//            System.out.println("❌ Không có dữ liệu phòng.");
//            return;
//        }
//
//        List<Room> rooms = roomXML.getRooms();
//        List<Booking> bookings = bookingXML != null && bookingXML.getBookings() != null ? bookingXML.getBookings() : new ArrayList<>();
//        LocalDateTime now = LocalDateTime.now();
//
//        for (Room room : rooms) {
//            boolean isActive = false;
//
//            for (Booking booking : bookings) {
//                if (booking.getRoomId().equals(room.getRoomId())
//                        && "Check-in".equalsIgnoreCase(booking.getStatus())
//                        && !now.isBefore(booking.getCheckIn())
//                        && !now.isAfter(booking.getCheckOut())) {
//                    isActive = true;
//                    break;
//                }
//            }
//
//            System.out.println("🏨 Phòng " + room.getRoomId() + ": " + (isActive ? "🟢 Đang hoạt động" : "⚪ Không hoạt động"));
//        }
//    }
//}
