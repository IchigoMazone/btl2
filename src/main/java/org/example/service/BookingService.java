package org.example.service;

import org.example.entity.*;
import org.example.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    // Tạo booking mới
    public static void createBooking(String filePath,
                                     String bookingId,
                                     String requestId,
                                     String userName,
                                     String fullName,
                                     String email,
                                     String phone,
                                     String roomId,
                                     LocalDateTime checkIn,
                                     LocalDateTime checkOut,
                                     double amount,
                                     List<Person> persons) {

        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null) {
            bookingXML = new BookingXML();
        }

        List<Booking> bookings = bookingXML.getBookings();
        if (bookings == null) {
            bookings = new ArrayList<>();
        }

        LocalDateTime now = LocalDateTime.now();

        // Khởi tạo lịch sử chỉ 1 trạng thái ban đầu
        List<HistoryEntry> historyList = new ArrayList<>();
        historyList.add(new HistoryEntry(now, "Đã tạo booking"));

        // Tạo đối tượng Booking mới với trạng thái ban đầu "Chờ duyệt" hoặc "Đã tạo booking"
        Booking newBooking = new Booking(
                bookingId,
                requestId,
                userName,
                fullName,
                email,
                phone,
                roomId,
                checkIn,
                checkOut,
                amount,
                now,
                "Chờ duyệt", // trạng thái ban đầu
                persons,
                historyList
        );

        bookings.add(0, newBooking); // Thêm booking mới lên đầu danh sách
        bookingXML.setBookings(bookings);
        FileUtils.writeToFile(filePath, bookingXML);

        System.out.println("Tạo booking thành công: " + bookingId);
    }

    // Cập nhật trạng thái booking theo bookingId
    public static void updateBookingStatus(String filePath, String bookingId, String newStatus) {
        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            System.out.println("Không có dữ liệu booking.");
            return;
        }

        boolean updated = false;
        for (Booking booking : bookingXML.getBookings()) {
            if (booking.getBookingId().equals(bookingId)) {
                booking.setStatus(newStatus);
                booking.addBookingHistory(newStatus, LocalDateTime.now());
                updated = true;
                break;
            }
        }

        if (updated) {
            FileUtils.writeToFile(filePath, bookingXML);
            System.out.println("Cập nhật trạng thái thành công: " + bookingId);
        } else {
            System.out.println("Không tìm thấy booking với ID: " + bookingId);
        }
    }

    // Liệt kê danh sách khách đang lưu trú với số điện thoại và email đại diện
    public static void listCurrentGuests(LocalDateTime currentTime, String filePath) {
        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            System.out.println("Không có dữ liệu booking.");
            return;
        }

        List<Booking> currentBookings = bookingXML.getBookings().stream()
                .filter(b -> !b.getCheckIn().isAfter(currentTime) && !b.getCheckOut().isBefore(currentTime))
                .toList();

        if (currentBookings.isEmpty()) {
            System.out.println("Không có khách nào đang lưu trú vào thời điểm " + currentTime);
            return;
        }

        System.out.println("Danh sách khách đang lưu trú tại " + currentTime + ":");
        for (Booking booking : currentBookings) {
            String phone = booking.getPhone();
            String email = booking.getEmail();

            List<Person> persons = booking.getPersons();
            for (Person person : persons) {
                System.out.println("- " + person.getName()
                        + " | SĐT đại diện: " + phone
                        + " | Email đại diện: " + email
                        + " | Phòng: " + booking.getRoomId()
                        + " | Check-in: " + booking.getCheckIn()
                        + " | Check-out: " + booking.getCheckOut());
            }
        }
    }

    // Kiểm tra trạng thái hoạt động của các phòng
    public static void checkBookingStatus(String bookingFilePath, String roomFilePath) {
        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);

        if (roomXML == null || roomXML.getRooms() == null || roomXML.getRooms().isEmpty()) {
            System.out.println("Không có dữ liệu phòng.");
            return;
        }

        List<Room> rooms = roomXML.getRooms();
        List<Booking> bookings = bookingXML != null && bookingXML.getBookings() != null ? bookingXML.getBookings() : new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Room room : rooms) {
            boolean isActive = false;

            for (Booking booking : bookings) {
                if (booking.getRoomId().equals(room.getRoomId())
                        && "Check-in".equalsIgnoreCase(booking.getStatus())
                        && !now.isBefore(booking.getCheckIn())
                        && !now.isAfter(booking.getCheckOut())) {
                    isActive = true;
                    break;
                }
            }

            System.out.println("Phòng " + room.getRoomId() + ": " + (isActive ? "Đang hoạt động" : "Không hoạt động"));
        }
    }
}
