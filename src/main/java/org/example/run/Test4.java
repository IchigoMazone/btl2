package org.example.run;

import org.example.service.RoomFinderService;
import org.example.entity.Room;
import java.time.LocalDateTime;
import java.util.List;

public class Test4 {
    public static void main(String[] args) {
        // Đường dẫn file dữ liệu
        String roomPath = "rooms.xml";
        String bookingPath = "bookings.xml";

        // Khởi tạo service tìm phòng
        RoomFinderService finder = new RoomFinderService(roomPath, bookingPath);

        // Khoảng thời gian cần tìm phòng
        LocalDateTime checkIn = LocalDateTime.of(2025, 7, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 7, 12, 12, 0);
        String roomType = "Phòng đơn";

        // Tìm phòng trống
        List<Room> availableRooms = finder.findAvailableRooms(checkIn, checkOut, roomType);

        // In kết quả
        if (availableRooms.isEmpty()) {
            System.out.println("Không có phòng trống phù hợp.");
        } else {
            System.out.println("Danh sách phòng trống:");
            for (Room room : availableRooms) {
                System.out.println("- Phòng " + room.getRoomId() + " | " + room.getDescription() + " | Giá: " + room.getPrice());
            }
        }
    }
}

