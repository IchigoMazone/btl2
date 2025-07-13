package org.example.service;

import org.example.entity.Room;
import org.example.entity.RoomXML;
import org.example.entity.Booking;
import org.example.entity.BookingXML;
import org.example.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service xử lý nghiệp vụ tìm phòng trống theo yêu cầu.
 */
public class RoomFinderService {

    private final String roomFilePath;
    private final String bookingFilePath;

    public RoomFinderService(String roomFilePath, String bookingFilePath) {
        this.roomFilePath = roomFilePath;
        this.bookingFilePath = bookingFilePath;
    }

    /**
     * Tìm danh sách phòng trống theo khoảng thời gian và loại phòng.
     * @param checkIn  thời gian nhận phòng
     * @param checkOut thời gian trả phòng
     * @param roomType loại phòng cần tìm
     * @return danh sách phòng trống phù hợp
     */
    public List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, String roomType) {
        List<Room> allRooms = getAllRooms();
        List<Booking> allBookings = getAllBookings();

        List<Room> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            // Bỏ qua phòng không đúng loại
            if (!room.getType().equalsIgnoreCase(roomType)) {
                continue;
            }

            boolean isAvailable = true;
            for (Booking booking : allBookings) {
                if (!booking.getRoomId().equals(room.getRoomId())) {
                    continue;
                }

                // Nếu thời gian booking trùng hoặc chồng lên khoảng thời gian cần tìm
                if (isOverlapping(booking.getCheckIn(), booking.getCheckOut(), checkIn, checkOut)) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    /**
     * Kiểm tra hai khoảng thời gian có chồng lấp nhau hay không.
     * @param aStart thời gian bắt đầu khoảng A
     * @param aEnd   thời gian kết thúc khoảng A
     * @param bStart thời gian bắt đầu khoảng B
     * @param bEnd   thời gian kết thúc khoảng B
     * @return true nếu chồng lấp, false nếu không
     */
    private boolean isOverlapping(LocalDateTime aStart, LocalDateTime aEnd,
                                  LocalDateTime bStart, LocalDateTime bEnd) {
        // aEnd trước bStart hoặc aStart sau bEnd => không chồng lấp
        return !(aEnd.isBefore(bStart) || aStart.isAfter(bEnd));
    }

    /**
     * Đọc tất cả phòng từ file XML.
     * @return danh sách phòng
     */
    private List<Room> getAllRooms() {
        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);
        if (roomXML == null) {
            return new ArrayList<>();
        }
        return roomXML.getRooms();
    }

    /**
     * Đọc tất cả booking từ file XML.
     * @return danh sách booking
     */
    private List<Booking> getAllBookings() {
        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
        if (bookingXML == null) {
            return new ArrayList<>();
        }
        return bookingXML.getBookings();
    }
}