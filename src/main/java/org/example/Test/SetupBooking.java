package org.example.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupBooking {

    // Tạo booking mới
    public static void createBooking(String filePath, String requestId, String username,
                                     String roomId, LocalDateTime checkIn,
                                     LocalDateTime checkOut, double amount) {
        BookingXML bookingXML = XMLUtil.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null) {
            bookingXML = new BookingXML();
        }

        List<Booking> bookings = bookingXML.getBookings();
        Booking newBooking = new Booking();
        newBooking.setBookingId("BK" + System.currentTimeMillis());
        newBooking.setRequestId(requestId);
        newBooking.setUserName(username);
        newBooking.setRoomId(roomId);
        newBooking.setCheckIn(checkIn);
        newBooking.setCheckOut(checkOut);
        newBooking.setCreatedAt(LocalDateTime.now());
        newBooking.setStatus("Đã tạo"); // Unicode OK
        newBooking.setAmount(amount);
        newBooking.addHistory("Đã tạo", LocalDateTime.now());

        bookings.add(newBooking);
        bookingXML.setBookings(bookings);
        XMLUtil.writeToFile(filePath, bookingXML);
    }

    // Cập nhật trạng thái booking
    public static void updateBookingStatus(String filePath, String userName, String status) {
        BookingXML bookingXML = XMLUtil.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null) return;

        boolean updated = false;
        for (Booking b : bookingXML.getBookings()) {
            if (b.getUserName().equals(userName)) {
                b.setStatus(status);
                b.addHistory(status, LocalDateTime.now());
                updated = true;
            }
        }

        if (updated) {
            XMLUtil.writeToFile(filePath, bookingXML);
        }
    }

    // Kiểm tra trạng thái hoạt động của tất cả phòng
    public static void checkBookingStatus(String bookingPath, String roomPath) {
        BookingXML bookingXML = XMLUtil.readFromFile(bookingPath, BookingXML.class);
        RoomXML roomXML = XMLUtil.readFromFile(roomPath, RoomXML.class);

        if (roomXML == null) {
            System.out.println("Không có dữ liệu phòng.");
            return;
        }

        List<Room> roomList = roomXML.getRooms();
        List<Booking> bookingList = bookingXML != null ? bookingXML.getBookings() : List.of();

        LocalDateTime now = LocalDateTime.now();

        // Nhóm các booking theo roomId
        Map<String, List<Booking>> bookingsByRoom = new HashMap<>();
        for (Booking booking : bookingList) {
            bookingsByRoom
                    .computeIfAbsent(booking.getRoomId(), key -> new ArrayList<>())
                    .add(booking);
        }

        for (Room room : roomList) {
            String roomId = room.getRoomId();
            boolean isActive = false;

            List<Booking> bookingsForRoom = bookingsByRoom.getOrDefault(roomId, List.of());

            for (Booking booking : bookingsForRoom) {
                LocalDateTime checkIn = booking.getCheckIn();
                LocalDateTime checkOut = booking.getCheckOut();
                String status = booking.getStatus();

                boolean currentlyActive = checkIn != null && checkOut != null &&
                        checkIn.isBefore(now) &&
                        (now.isBefore(checkOut) || now.isEqual(checkOut)) &&
                        "CheckIn".equalsIgnoreCase(status);

                if (currentlyActive) {
                    isActive = true;
                    break;
                }
            }

            // ✅ Chỉ in ra tiếng Việt
            System.out.println("Phòng " + roomId + ": " + (isActive ? "Đang hoạt động" : "Không hoạt động"));
        }
    }
}
