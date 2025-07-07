package org.example.Test;

import java.time.LocalDateTime;
import java.util.List;

public class Test4 {
    public static void main(String[] args) {
        // File paths
        String roomPath = "rooms.xml";
        String bookingPath = "bookings.xml";

        // Khởi tạo RoomFinderService
        RoomFinderService finder = new RoomFinderService(roomPath, bookingPath);

        // Thời gian tìm phòng
        LocalDateTime checkIn = LocalDateTime.of(2025, 7, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 7, 12, 12, 0);
        String roomType = "Phòng đơn";

        // Tìm phòng
        List<Room> availableRooms = finder.findAvailableRooms(checkIn, checkOut, roomType);

        // In kết quả
        if (availableRooms.isEmpty()) {
            System.out.println("❌ Không có phòng trống phù hợp.");
        } else {
            System.out.println("✅ Danh sách phòng trống:");
            for (Room room : availableRooms) {
                System.out.println("- Phòng " + room.getRoomId() + " | " + room.getDescription() + " | Giá: " + room.getPrice());
            }
        }
    }
}
