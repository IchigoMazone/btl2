//package org.example.Test;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class RoomFinderService {
//
//    private final String roomFilePath;
//    private final String bookingFilePath;
//
//    public RoomFinderService(String roomFilePath, String bookingFilePath) {
//        this.roomFilePath = roomFilePath;
//        this.bookingFilePath = bookingFilePath;
//    }
//
//    // Tìm phòng trống theo thời gian và loại phòng
//    public List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, String roomType) {
//        List<Room> allRooms = getAllRooms();
//        List<Booking> allBookings = getAllBookings();
//
//        List<Room> availableRooms = new ArrayList<>();
//
//        for (Room room : allRooms) {
//            if (!room.getType().equalsIgnoreCase(roomType)) continue;
//
//            boolean isAvailable = true;
//            for (Booking booking : allBookings) {
//                if (!booking.getRoomId().equals(room.getRoomId())) continue;
//
//                // Kiểm tra nếu thời gian đặt hiện tại bị chồng với yêu cầu
//                if (isOverlapping(booking.getCheckIn(), booking.getCheckOut(), checkIn, checkOut)) {
//                    isAvailable = false;
//                    break;
//                }
//            }
//
//            if (isAvailable) {
//                availableRooms.add(room);
//            }
//        }
//
//        return availableRooms;
//    }
//
//    // Hàm kiểm tra thời gian chồng nhau
//    private boolean isOverlapping(LocalDateTime aStart, LocalDateTime aEnd,
//                                  LocalDateTime bStart, LocalDateTime bEnd) {
//        return !(aEnd.isBefore(bStart) || aStart.isAfter(bEnd));
//    }
//
//    private List<Room> getAllRooms() {
//        RoomXML roomXML = XMLUtil.readFromFile(roomFilePath, RoomXML.class);
//        return roomXML != null ? roomXML.getRooms() : new ArrayList<>();
//    }
//
////    private List<Booking> getAllBookings() {
////        BookingXML bookingXML = XMLUtil.readFromFile(bookingFilePath, BookingXML.class);
////        return bookingXML != null ? bookingXML.getBookings() : new ArrayList<>();
////    }
//}
//
