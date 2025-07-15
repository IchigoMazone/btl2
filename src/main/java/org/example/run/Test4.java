
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
        LocalDateTime checkIn = LocalDateTime.of(2025, 7, 20, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 7, 22, 12, 0);

        // Gọi tìm kiếm với loại phòng là "Tất cả"
        List<Room> availableRooms = finder.findAvailableRooms(checkIn, checkOut, "Phòng đơn");

        // In kết quả
        if (availableRooms.isEmpty()) {
            System.out.println("❌ Không có phòng trống phù hợp.");
        } else {
            System.out.println("✅ Danh sách phòng trống:");
            for (Room room : availableRooms) {
                System.out.printf("- Mã: %s | Loại: %s | Mô tả: %s | Giá: %.2f\n",
                        room.getRoomId(), room.getType(), room.getDescription(), room.getPrice());
            }
        }
    }
}

